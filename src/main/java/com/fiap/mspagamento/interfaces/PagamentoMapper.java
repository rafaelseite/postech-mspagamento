package com.fiap.mspagamento.interfaces;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.entities.PagamentoEntity;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;

import java.time.LocalDateTime;
import java.util.UUID;

public class PagamentoMapper {

    public static Pagamento toValueObject(PagamentoEntity entity) {
        return new Pagamento(
                entity.getId(),
                entity.getPedidoId(),
                entity.getNumeroCartao(),
                entity.getValorTotal(),
                StatusPagamento.valueOf(entity.getStatus()),
                entity.getCriadoEm()
        );
    }

    public static PagamentoEntity toEntity(Pagamento pagamento) {
        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(pagamento.getId());
        entity.setPedidoId(pagamento.getPedidoId());
        entity.setNumeroCartao(pagamento.getNumeroCartao());
        entity.setValorTotal(pagamento.getValorTotal());
        entity.setStatus(pagamento.getStatus().name());
        entity.setCriadoEm(pagamento.getCriadoEm());
        return entity;
    }

    public static Pagamento toValueObject(PagamentoRequest request) {
        return new Pagamento(
                UUID.randomUUID(),
                request.getPedidoId(),
                request.getNumeroCartao(),
                request.getValorTotal(),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );
    }



    public static PagamentoResponse toResponse(Pagamento pagamento) {
        String statusPedido = switch (pagamento.getStatus()) {
            case SUCESSO -> "PROCESSADO_SUCESSO";
            case FALHA_CARTAO -> "PROCESSADO_SEM_CREDITO";
            default -> "PROCESSADO_ERRO";
        };

        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValorTotal(),
                statusPedido,
                pagamento.getCriadoEm()
        );
    }

/*
    public static PagamentoResponse toResponse(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValorTotal(),
                pagamento.getStatus().name(),
                pagamento.getCriadoEm()
        );
    }*/
}
