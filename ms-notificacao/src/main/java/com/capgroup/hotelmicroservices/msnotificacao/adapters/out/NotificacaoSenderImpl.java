package com.capgroup.hotelmicroservices.msnotificacao.adapters.out;
import com.capgroup.hotelmicroservices.msnotificacao.core.domain.Notificacao;
import com.capgroup.hotelmicroservices.msnotificacao.ports.out.dtos.ReservaEventoDto;
import com.capgroup.hotelmicroservices.msnotificacao.ports.out.NotificacaoRepository;
import com.capgroup.hotelmicroservices.msnotificacao.ports.out.NotificacaoSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NotificacaoSenderImpl implements NotificacaoSender {

    private final JavaMailSender mailSender;
    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoSenderImpl(JavaMailSender mailSender, NotificacaoRepository notificacaoRepository) {
        this.mailSender = mailSender;
        this.notificacaoRepository = notificacaoRepository;
    }

    @Override
    public void enviarNotificacoes(ReservaEventoDto evento) {
        log.info("Processando evento: {} para Reserva ID: {}", evento.tipo(), evento.id());

        // Envia notificação para o Hóspede
        processarEnvio(evento.emailHospede(), evento.nomeHospede(), evento.tipo(), evento);

        // Envia notificação para o Proprietário
        processarEnvio(evento.emailProprietario(), evento.nomeProprietario(), evento.tipo(), evento);
    }

    private void processarEnvio(String destinatario, String nomeDestinatario, String tipo, ReservaEventoDto evento) {
        String assunto;
        String corpo;

        switch (tipo) {
            case "CRIACAO":
                assunto = "Sua Reserva foi Confirmada!";
                corpo = criarCorpoConfirmacao(nomeDestinatario, evento);
                break;
            case "ALTERACAO":
                assunto = "Sua Reserva foi Atualizada.";
                corpo = criarCorpoAlteracao(nomeDestinatario, evento);
                break;
            case "CANCELAMENTO":
                assunto = "Aviso de Cancelamento de Reserva.";
                corpo = criarCorpoCancelamento(nomeDestinatario, evento);
                break;
            case "LEMBRETE":
                assunto = "Lembrete: Seu Check-in está próximo!";
                corpo = criarCorpoLembrete(nomeDestinatario, evento);
                break;
            default:
                log.warn("Tipo de evento desconhecido: {}", tipo);
                return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@checkin.com");
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(corpo);

        // 1. Criação do Registro de Notificação (PENDENTE)
        Notificacao notificacao = new Notificacao();
        notificacao.setReservaId(evento.id());
        notificacao.setDestinatario(destinatario);
        notificacao.setTipoEvento(tipo);
        notificacao.setAssunto(assunto);
        notificacao.setDataRecebimento(evento.dataEnvio());
        notificacao.setStatusEnvio(Notificacao.StatusEnvio.PENDENTE);

        try {
            // 2. Tenta o envio real
            mailSender.send(message);

            // 3. Sucesso: Atualização do Registro
            notificacao.setDataEnvio(LocalDateTime.now());
            notificacao.setStatusEnvio(Notificacao.StatusEnvio.ENVIADO);

            log.info("E-mail tipo {} enviado com sucesso para: {}", tipo, destinatario);
        } catch (Exception e) {
            // 3. Falha: Atualização do Registro
            notificacao.setStatusEnvio(Notificacao.StatusEnvio.FALHA);
            notificacao.setLogErro(e.getMessage());
            log.error("Falha ao enviar e-mail para {}. Causa: {}", destinatario, e.getMessage());
        } finally {
            // 4. Persistência final do status
            notificacaoRepository.save(notificacao);
        }
    }

    // --- Métodos Auxiliares de Criação de Corpo de E-mail ---
    private String criarCorpoConfirmacao(String nome, ReservaEventoDto evento) {
        return String.format(
                "Olá %s,\n\nSua reserva para a propriedade '%s' foi confirmada com sucesso!\nCheck-in: %s | Check-out: %s.\nTotal: R$ %s.\n\nObrigado por escolher nossos serviços.",
                nome, evento.nomePropriedade(), evento.checkIn(), evento.checkOut(), evento.valorTotal()
        );
    }

    private String criarCorpoAlteracao(String nome, ReservaEventoDto evento) {
        return String.format(
                "Olá %s,\n\nHouve uma alteração na sua reserva para '%s'.\nNovas Datas: %s a %s.\n\nPor favor, verifique os detalhes.",
                nome, evento.nomePropriedade(), evento.checkIn(), evento.checkOut()
        );
    }

    private String criarCorpoCancelamento(String nome, ReservaEventoDto evento) {
        return String.format(
                "Olá %s,\n\nA reserva ID %s para '%s' foi CANCELADA.\nLamentamos o inconveniente.",
                nome, evento.id(), evento.nomePropriedade()
        );
    }

    private String criarCorpoLembrete(String nome, ReservaEventoDto evento) {
        return String.format(
                "Olá %s,\n\nEste é um lembrete amigável! Seu check-in na propriedade '%s' será em breve, no dia %s.\n\nEsperamos você!",
                nome, evento.nomePropriedade(), evento.checkIn()
        );
    }
}