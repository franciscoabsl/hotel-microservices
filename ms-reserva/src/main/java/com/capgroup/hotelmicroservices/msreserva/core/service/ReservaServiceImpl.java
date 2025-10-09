package com.capgroup.hotelmicroservices.msreserva.core.service;

import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaResponseDto;
import com.capgroup.hotelmicroservices.msreserva.ports.out.AuthUserClient;
import com.capgroup.hotelmicroservices.msreserva.ports.out.PropriedadeClient;
import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.QuartoDetalheDto;
import com.capgroup.hotelmicroservices.msreserva.adapters.in.dtos.ReservaInputDto;
import com.capgroup.hotelmicroservices.msreserva.ports.out.dtos.UsuarioDetalheDto;
import com.capgroup.hotelmicroservices.msreserva.core.domain.exceptions.AcessoNegadoException;
import com.capgroup.hotelmicroservices.msreserva.core.domain.exceptions.RecursoNaoDisponivelException;
import com.capgroup.hotelmicroservices.msreserva.core.domain.exceptions.RecursoNaoEncontradoException;
import com.capgroup.hotelmicroservices.msreserva.core.domain.Reserva;
import com.capgroup.hotelmicroservices.msreserva.core.domain.ReservaStatusEnum;
import com.capgroup.hotelmicroservices.msreserva.ports.out.ReservaRepository;
import com.capgroup.hotelmicroservices.msreserva.ports.out.RabbitMQSender;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final RabbitMQSender rabbitMQSender;
    private final PropriedadeClient propriedadeClient; // Injeção da Interface
    private final AuthUserClient authUserClient;       // Injeção da Interface

    private static final String PROP_CB = "propriedadeServiceCB";
    private static final String AUTH_CB = "authServiceCB";
    private static final String ROLE_ADMIN = "ADMIN";

    // Injeção via construtor (Lombok @RequiredArgsConstructor ou manual)
    public ReservaServiceImpl(
            ReservaRepository reservaRepository,
            RabbitMQSender rabbitMQSender,
            PropriedadeClient propriedadeClient,
            AuthUserClient authUserClient) {
        this.reservaRepository = reservaRepository;
        this.rabbitMQSender = rabbitMQSender;
        this.propriedadeClient = propriedadeClient;
        this.authUserClient = authUserClient;
    }

    // --- MÉTODOS DE ESCRITA E FLUXO CRÍTICO ---

    @Override
    public ReservaResponseDto criarReserva(UUID quartoId, ReservaInputDto inputDto, UUID userId) {
        log.info("Iniciando fluxo de criação de reserva para Quarto ID: {}. Usuário: {}", quartoId, userId);

        // 1. VALIDAÇÃO DE DISPONIBILIDADE
        if (reservaRepository.existsByQuartoIdAndCheckInBeforeAndCheckOutAfter(
                quartoId, inputDto.checkOut(), inputDto.checkIn())) {
            throw new RecursoNaoDisponivelException("O quarto não está disponível para o período solicitado.");
        }

        // 2 & 3. COLETA DE DADOS COM RESILIÊNCIA (Circuit Breaker)
        QuartoDetalheDto quarto = getQuartoDetails(quartoId);
        UsuarioDetalheDto hospede = getUsuarioDetails(userId);

        // 4. AGREGAÇÃO E CÁLCULO
        Reserva novaReserva = montarReserva(quarto, hospede, inputDto);
        novaReserva.setValorTotal(calcularValor(quarto.valorDiaria(), inputDto));

        // 5. PERSISTÊNCIA
        Reserva reservaSalva = reservaRepository.save(novaReserva);
        log.info("Reserva salva com sucesso. ID: {}", reservaSalva.getId());

        // 6. NOTIFICAÇÃO (Assíncrona - Evento Rico)
        rabbitMQSender.sendReservaEvent(reservaSalva, "CRIACAO");
        log.info("Evento RESERVA_CRIADA enviado ao RabbitMQ.");

        return mapToReservaResponseDto(reservaSalva);
    }

    // --- 2. Atualizar Reserva  ---
    @Override
    public ReservaResponseDto atualizarReserva(UUID reservaId, ReservaInputDto inputDto, UUID userId) {
        log.info("Iniciando atualização da reserva ID: {} pelo usuário ID: {}", reservaId, userId);

        Reserva reserva = buscarReservaParaAtualizacao(reservaId, userId, "HOSPEDE");

        if (reserva.getCheckIn().isEqual(inputDto.checkIn()) && reserva.getCheckOut().isEqual(inputDto.checkOut())) {
            return mapToReservaResponseDto(reserva);
        }

        if (existsConflictByQuartoIdExcludingId(reserva.getQuartoId(), reservaId, inputDto.checkIn(), inputDto.checkOut())) {
            throw new RecursoNaoDisponivelException("A data de check-out deve ser posterior ao check-in.");
        }

        // 3. Atualização de Dados
        reserva.setCheckIn(inputDto.checkIn());
        reserva.setCheckOut(inputDto.checkOut());

        // Recálculo do valor: Busca o valor da diária novamente (caso tenha mudado)
        QuartoDetalheDto quarto = getQuartoDetails(reserva.getQuartoId());
        reserva.setValorTotal(calcularValor(quarto.valorDiaria(), inputDto));

        // 4. Persistência e Notificação
        Reserva reservaAtualizada = reservaRepository.save(reserva);
        rabbitMQSender.sendReservaEvent(reservaAtualizada, "ALTERACAO");
        log.info("Reserva ID {} atualizada e evento ALTERACAO enviado.", reservaId);

        return mapToReservaResponseDto(reservaAtualizada);
    }

    @Override
    public ReservaResponseDto cancelarReserva(UUID reservaId, UUID userId) {
        Reserva reserva = buscarReservaPorId(reservaId);

        boolean isAuthorized = reserva.getHospedeId().equals(userId) || reserva.getProprietarioId().equals(userId);

        if (!isAuthorized) {
            throw new AcessoNegadoException("Usuário não tem permissão para cancelar esta reserva.");
        }

        reserva.setStatus(ReservaStatusEnum.CANCELADA);
        Reserva reservaCancelada = reservaRepository.save(reserva);

        rabbitMQSender.sendReservaEvent(reservaCancelada, "CANCELAMENTO");
        log.info("Reserva ID {} cancelada e evento CANCELAMENTO enviado.", reservaId);

        return mapToReservaResponseDto(reservaCancelada);
    }

    // --- MÉTODOS DE LEITURA E AUTORIZAÇÃO ---

    @Override
    public Reserva buscarReservaPorId(UUID reservaId) {
        log.info("Buscando reserva por ID: {}", reservaId);
        return reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada com ID: " + reservaId));
    }

    @Override
    public ReservaResponseDto buscarReservaAutorizada(UUID reservaId, UUID userId, String userRoles) {
        Reserva reserva = buscarReservaPorId(reservaId);

        boolean isHospedeProprietario = reserva.getHospedeId().equals(userId) || reserva.getProprietarioId().equals(userId);
        boolean isAdmin = userRoles != null && userRoles.contains(ROLE_ADMIN);

        // 2. Verifica Autorização
        if (!isHospedeProprietario && !isAdmin) {
            log.warn("Acesso negado à reserva {}. Usuário ID {} não é dono nem ADMIN.", reservaId, userId);
            throw new AcessoNegadoException("Você não tem permissão para visualizar esta reserva.");
        }

        return mapToReservaResponseDto(reserva);
    }

    @Override
    public Reserva buscarReservaParaAtualizacao(UUID reservaId, UUID userId, String userRoles) {
        Reserva reserva = buscarReservaPorId(reservaId);

        boolean isHospedeOuProprietario = reserva.getHospedeId().equals(userId) || reserva.getProprietarioId().equals(userId);

        if (!isHospedeOuProprietario) {
            log.warn("Acesso negado à reserva {}. Usuário ID {} não é dono nem ADMIN.", reservaId, userId);
            throw new AcessoNegadoException("Você não tem permissão para visualizar esta reserva.");
        }

        return reserva;
    }

    // --- MÉTODOS DE RESILIÊNCIA E FALLBACK (Usando as Interfaces de Cliente) ---

    @CircuitBreaker(name = PROP_CB, fallbackMethod = "fallbackGetQuarto")
    private QuartoDetalheDto getQuartoDetails(UUID quartoId) {
        // Usa a Interface (que internamente usa o WebClient)
        return propriedadeClient.getQuartoDetalhe(quartoId);
    }

    @CircuitBreaker(name = AUTH_CB, fallbackMethod = "fallbackGetUsuario")
    private UsuarioDetalheDto getUsuarioDetails(UUID userId) {
        // Usa a Interface (que internamente usa o WebClient)
        return authUserClient.getUsuarioDetalhe(userId);
    }

    private QuartoDetalheDto fallbackGetQuarto(UUID quartoId, Throwable t) {
        log.error("Circuit Breaker aberto ou falha ao buscar Quarto ID {}. Causa: {}", quartoId, t.getMessage());
        throw new RecursoNaoDisponivelException("Serviço de Propriedades indisponível. Tente novamente mais tarde.");
    }

    private UsuarioDetalheDto fallbackGetUsuario(UUID userId, Throwable t) {
        log.error("Circuit Breaker aberto ou falha ao buscar Usuário ID {}. Causa: {}", userId, t.getMessage());
        throw new RecursoNaoDisponivelException("Serviço de Autenticação/Usuário indisponível. Tente novamente mais tarde.");
    }

    // --- LÓGICA DE AGENDAMENTO (EVENTO LEMBRETE) ---

    @Scheduled(cron = "0 0 8 * * *")
    public void publicarLembretesDiarios() {
        log.info("Executando job de verificação de lembretes diários.");
        // Implementação: buscar reservas com check-in próximo e enviar o evento LEMBRETE.
    }

    // --- MÉTODOS AUXILIARES ---

    private  boolean existsConflictByQuartoIdExcludingId(UUID quartoId, UUID reservaId, LocalDate checkIn, LocalDate checkOut) {
        return  reservaRepository.existsConflictByQuartoIdExcludingId(quartoId, reservaId, checkIn, checkOut);
    }

    private ReservaResponseDto mapToReservaResponseDto(Reserva reserva) {
        return new ReservaResponseDto(
                reserva.getId(),
                reserva.getQuartoId(),
                reserva.getCheckIn(),
                reserva.getCheckOut(),
                reserva.getValorTotal(),
                reserva.getNomeHospede(),
                reserva.getHospedeId(),
                reserva.getEmailHospede(),
                reserva.getPropriedadeId(),
                reserva.getNomePropriedade(),
                reserva.getNomeQuarto(),
                reserva.getNomeProprietario(),
                reserva.getProprietarioId()
        );
    }

    private Reserva montarReserva(QuartoDetalheDto quarto, UsuarioDetalheDto hospede, ReservaInputDto input) {
        Reserva r = new Reserva();
        r.setQuartoId(quarto.id());
        r.setHospedeId(hospede.id());
        r.setCheckIn(input.checkIn());
        r.setCheckOut(input.checkOut());
        r.setNomeHospede(hospede.nome());
        r.setProprietarioId(quarto.proprietarioId());
        r.setNomeProprietario(quarto.nomePropriedade()); // Dados ricos
        r.setNomePropriedade(quarto.nomePropriedade());
        r.setNomeQuarto(quarto.nomeQuarto());
        r.setValorTotal(calcularValor(quarto.valorDiaria(), input));

        return r;
    }

    private BigDecimal calcularValor(BigDecimal valorDiaria, ReservaInputDto input) {
        long dias = ChronoUnit.DAYS.between(input.checkIn(), input.checkOut());
        if (dias <= 0) {
            throw new IllegalArgumentException("Check-out deve ser posterior ao check-in.");
        }
        return valorDiaria.multiply(BigDecimal.valueOf(dias));
    }
}