package com.capgroup.hotelmicroservices.mspropriedade.repository;

import com.capgroup.hotelmicroservices.mspropriedade.domain.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PropriedadeRepository extends JpaRepository<Propriedade, UUID> {
}