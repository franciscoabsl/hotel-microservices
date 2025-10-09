package com.capgroup.hotelmicroservices.msnotificacao.ports.out;

import com.capgroup.hotelmicroservices.msnotificacao.core.domain.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {

}
