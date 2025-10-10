package com.capgroup.hotelmicroservices.mspropriedade.repository;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, UUID> {

    List<Quarto> findByPropriedadeId(UUID propriedadeId);
}