package com.capgroup.hotelmicroservices.msnotificacao.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TB_NOTIFICACOES")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Identificador da reserva original
    private UUID reservaId;

    // Dados essenciais para o registro
    private String destinatario;
    private String tipoEvento; // CRIACAO, CANCELAMENTO, etc.
    private String assunto;

    // Status do Envio
    @Enumerated(EnumType.STRING)
    private StatusEnvio statusEnvio;

    // Datas e Logs
    private LocalDateTime dataRecebimento;
    private LocalDateTime dataEnvio;
    private String logErro; // Para registrar falhas de SMTP

    public enum StatusEnvio {
        PENDENTE,
        ENVIADO,
        FALHA
    }
}
