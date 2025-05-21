package com.fiap.mspagamento.interfaces;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.entities.PagamentoEntity;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PagamentoMapperTest {

    @Test
    void testToValueObjectFromEntity() {
        PagamentoEntity entity = new PagamentoEntity();
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        entity.setId(id);
        entity.setPedidoId(pedidoId);
        entity.setNumeroCartao("1234567890123456");
        entity.setValorTotal(new BigDecimal("150.00"));
        entity.setStatus("SUCESSO");
        entity.setCriadoEm(LocalDateTime.now());

        Pagamento pagamento = PagamentoMapper.toValueObject(entity);

        assertEquals(id, pagamento.getId());
        assertEquals(pedidoId, pagamento.getPedidoId());
        assertEquals("1234567890123456", pagamento.getNumeroCartao());
        assertEquals(new BigDecimal("150.00"), pagamento.getValorTotal());
        assertEquals(StatusPagamento.SUCESSO, pagamento.getStatus());
        assertNotNull(pagamento.getCriadoEm());
    }

    @Test
    void testToEntity() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        LocalDateTime criadoEm = LocalDateTime.now();

        Pagamento pagamento = new Pagamento(
                id,
                pedidoId,
                "1234567890123456",
                new BigDecimal("200.00"),
                StatusPagamento.FALHA_SISTEMA,
                criadoEm
        );

        PagamentoEntity entity = PagamentoMapper.toEntity(pagamento);

        assertEquals(id, entity.getId());
        assertEquals(pedidoId, entity.getPedidoId());
        assertEquals("1234567890123456", entity.getNumeroCartao());
        assertEquals(new BigDecimal("200.00"), entity.getValorTotal());
        assertEquals("FALHA_SISTEMA", entity.getStatus());
        assertEquals(criadoEm, entity.getCriadoEm());
    }

    @Test
    void testToValueObjectFromRequest() {
        UUID pedidoId = UUID.randomUUID();
        PagamentoRequest request = new PagamentoRequest();
        request.setPedidoId(pedidoId);
        request.setNumeroCartao("9876543210987654");
        request.setValorTotal(new BigDecimal("120.00"));

        Pagamento pagamento = PagamentoMapper.toValueObject(request);

        assertEquals(pedidoId, pagamento.getPedidoId());
        assertEquals("9876543210987654", pagamento.getNumeroCartao());
        assertEquals(new BigDecimal("120.00"), pagamento.getValorTotal());
        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
        assertNotNull(pagamento.getCriadoEm());
    }

    @Test
    void testToResponse() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        LocalDateTime criadoEm = LocalDateTime.now();
        BigDecimal valorTotal = new BigDecimal("300.00");

        Pagamento pagamento = new Pagamento(
                id,
                pedidoId,
                "5555666677778888",
                valorTotal,
                StatusPagamento.SUCESSO,
                criadoEm
        );

        PagamentoResponse response = PagamentoMapper.toResponse(pagamento);

        assertEquals(id, response.id());
        assertEquals(pedidoId, response.pedidoId());
        assertEquals(valorTotal, response.valorTotal());
        assertEquals("PROCESSADO_SUCESSO", response.status());
        assertEquals(criadoEm, response.criadoEm());

    }

    @Test
    void testToResponseComFalhaCartao() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "1111222233334444",
                new BigDecimal("75.00"),
                StatusPagamento.FALHA_CARTAO,
                LocalDateTime.now()
        );

        PagamentoResponse response = PagamentoMapper.toResponse(pagamento);

        assertEquals("PROCESSADO_SEM_CREDITO", response.status());
    }

    @Test
    void testToResponseComFalhaSistema() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "4444555566667777",
                new BigDecimal("99.99"),
                StatusPagamento.FALHA_SISTEMA,
                LocalDateTime.now()
        );

        PagamentoResponse response = PagamentoMapper.toResponse(pagamento);
        assertEquals("PROCESSADO_ERRO", response.status());
    }

    @Test
    void testToResponseComPendente() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "0000111122223333",
                new BigDecimal("49.99"),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        PagamentoResponse response = PagamentoMapper.toResponse(pagamento);
        assertEquals("PROCESSADO_ERRO", response.status());
    }


    @Test
    void testToResponseComErro() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "1111222233334444",
                new BigDecimal("50.00"),
                StatusPagamento.FALHA_OUTROS,
                LocalDateTime.now()
        );

        PagamentoResponse response = PagamentoMapper.toResponse(pagamento);

        assertEquals("PROCESSADO_ERRO", response.status());
    }
}
