package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscarPagamentoUseCaseTest {

    private BuscarPagamentoUseCase useCase;
    private PagamentoGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = mock(PagamentoGateway.class);
        useCase = new BuscarPagamentoUseCase(gateway);
    }

    @Test
    void deveRetornarPagamentoQuandoExistir() {
        UUID id = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, UUID.randomUUID(), "12345678901", new BigDecimal("100"), StatusPagamento.SUCESSO, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));

        Optional<Pagamento> resultado = useCase.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(pagamento, resultado.get());

        verify(gateway).buscarPorId(id);
    }

    @Test
    void deveRetornarVazioQuandoNaoExistirPagamento() {
        UUID id = UUID.randomUUID();

        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        Optional<Pagamento> resultado = useCase.buscarPorId(id);

        assertTrue(resultado.isEmpty());

        verify(gateway).buscarPorId(id);
    }
}
