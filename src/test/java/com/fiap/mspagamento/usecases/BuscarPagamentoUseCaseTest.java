package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
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

class BuscarPagamentoUseCaseTest {

    private BuscarPagamentoUseCase useCase;
    private PagamentoGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = mock(PagamentoGateway.class);
        useCase = new BuscarPagamentoUseCase(gateway);
    }

    @Test
    void deveRetornarPagamentoResponseQuandoExistir() {
        UUID id = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, UUID.randomUUID(), "12345678901", new BigDecimal("100.00"), StatusPagamento.SUCESSO, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));

        PagamentoResponse response = useCase.buscarPorId(id);

        assertNotNull(response);
        assertEquals(pagamento.getId(), response.id());
        assertEquals(pagamento.getPedidoId(), response.pedidoId());
        assertEquals(pagamento.getValorTotal(), response.valorTotal());

        verify(gateway).buscarPorId(id);
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoNaoExistir() {
        UUID id = UUID.randomUUID();

        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(PagamentoNaoEncontradoException.class, () -> useCase.buscarPorId(id));

        verify(gateway).buscarPorId(id);
    }
}
