package com.fiap.mspagamento.interfaces;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.entities.PagamentoEntity;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;

import java.time.LocalDateTime;

public class PagamentoMapper {

    public static Pagamento toValueObject(PagamentoEntity entity) {
        return new Pagamento(
                entity.getId(),
                entity.getPedidoId(),
                entity.getSku(),
                entity.getQuantidade(),
                entity.getNumeroCartao(),
                entity.getValor(),
                StatusPagamento.valueOf(entity.getStatus()),
                entity.getCriadoEm()
        );
    }

    public static PagamentoEntity toEntity(Pagamento pagamento) {
        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(pagamento.getId());
        entity.setPedidoId(pagamento.getPedidoId());
        entity.setSku(pagamento.getSku());
        entity.setQuantidade(pagamento.getQuantidade());
        entity.setNumeroCartao(pagamento.getNumeroCartao());
        entity.setValor(pagamento.getValor());
        entity.setStatus(pagamento.getStatus().name());
        entity.setCriadoEm(pagamento.getCriadoEm());
        return entity;
    }

    public static Pagamento toValueObject(PagamentoRequest request) {
        return new Pagamento(
                null,
                request.getPedidoId(),
                request.getSku(),
                request.getQuantidade(),
                request.getNumeroCartao(),
                request.getValor(),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );
    }

    public static PagamentoResponse toResponse(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValor(),
                pagamento.getStatus().name(),
                pagamento.getCriadoEm()
        );
    }
}
