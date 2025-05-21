package com.fiap.mspagamento.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoRequestTest {

    @Test
    void deveSetarECapturarTodosOsCamposCorretamente() {
        UUID pedidoId = UUID.randomUUID();
        String numeroCartao = "4111111111111111";
        BigDecimal valor = new BigDecimal("199.99");

        PagamentoRequest request = new PagamentoRequest();
        request.setPedidoId(pedidoId);
        request.setNumeroCartao(numeroCartao);
        request.setValorTotal(valor);

        assertEquals(pedidoId, request.getPedidoId());
        assertEquals(numeroCartao, request.getNumeroCartao());
        assertEquals(valor, request.getValorTotal());
    }
}
