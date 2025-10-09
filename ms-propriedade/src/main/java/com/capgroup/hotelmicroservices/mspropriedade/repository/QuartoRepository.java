package com.capgroup.hotelmicroservices.mspropriedade.repository;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Long> {
    List<Quarto> findByPropriedadeId(Long propriedadeId);
}