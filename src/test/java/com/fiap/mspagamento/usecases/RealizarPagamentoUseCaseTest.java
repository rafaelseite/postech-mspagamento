package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.external.EstoqueServiceClient;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RealizarPagamentoUseCaseTest {

    private PagamentoGateway gateway;
    private EstoqueServiceClient estoqueServiceClient;
    private RealizarPagamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(PagamentoGateway.class);
        estoqueServiceClient = mock(EstoqueServiceClient.class);
        useCase = new RealizarPagamentoUseCase(gateway, estoqueServiceClient);
    }

    @Test
    void deveProcessarPagamentoComSucesso() {
        UUID id = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(
                id,
                UUID.randomUUID(),
                "SKU123",
                2,
                "12345678901",
                new BigDecimal("100.00"),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        assertNotNull(response);
        assertEquals(StatusPagamento.SUCESSO.name(), response.status());
        verify(gateway).salvar(any());
        verifyNoInteractions(estoqueServiceClient);
    }

    @Test
    void deveReporEstoqueSePagamentoFalhar() {
        UUID id = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(
                id,
                UUID.randomUUID(),
                "SKU456",
                5,
                "12345678909",
                new BigDecimal("50.00"),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        assertNotNull(response);
        assertNotEquals(StatusPagamento.SUCESSO.name(), response.status());
        verify(estoqueServiceClient).devolverEstoque("SKU456", 5);
    }

    @Test
    void deveLancarExcecaoSePagamentoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(PagamentoNaoEncontradoException.class, () -> useCase.executar(id));
    }
}
