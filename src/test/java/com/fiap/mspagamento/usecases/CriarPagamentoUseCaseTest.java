package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarPagamentoUseCaseTest {

    private PagamentoGateway gateway;
    private CriarPagamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(PagamentoGateway.class);
        useCase = new CriarPagamentoUseCase(gateway);
    }

    @Test
    void deveCriarPagamentoComSucesso() {
        PagamentoRequest request = new PagamentoRequest();
        UUID pedidoId = UUID.randomUUID();
        request.setPedidoId(pedidoId);
        request.setNumeroCartao("12345678901");
        request.setValorTotal(new BigDecimal("150.00"));

        Pagamento pagamentoCriado = new Pagamento(
                UUID.randomUUID(),
                pedidoId,
                "12345678901",
                new BigDecimal("150.00"),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        when(gateway.salvar(any())).thenReturn(pagamentoCriado);

        Pagamento resultado = useCase.executar(request);

        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getPedidoId());
        assertEquals(StatusPagamento.PENDENTE, resultado.getStatus());
        verify(gateway).salvar(any());
    }
}
