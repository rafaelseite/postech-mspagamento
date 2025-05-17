package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
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
        request.setPedidoId(UUID.randomUUID());
        request.setSku("PROD123");
        request.setQuantidade(3);
        request.setNumeroCartao("12345678901");
        request.setValor(new BigDecimal("150.00"));

        when(gateway.salvar(any(Pagamento.class))).thenAnswer(invocation -> {
            Pagamento p = invocation.getArgument(0);
            return new Pagamento(
                    UUID.randomUUID(),
                    p.getPedidoId(),
                    p.getSku(),
                    p.getQuantidade(),
                    p.getNumeroCartao(),
                    p.getValor(),
                    p.getStatus(),
                    LocalDateTime.now()
            );
        });

        PagamentoResponse response = useCase.executar(request);

        assertNotNull(response);
        assertEquals(request.getPedidoId(), response.pedidoId());
        assertEquals(request.getValor(), response.valor());
        assertEquals(StatusPagamento.PENDENTE.name(), response.status());
    }
}
