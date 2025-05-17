package com.fiap.mspagamento.gateways.database.jpa;

import com.fiap.mspagamento.entities.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, UUID> {

}
