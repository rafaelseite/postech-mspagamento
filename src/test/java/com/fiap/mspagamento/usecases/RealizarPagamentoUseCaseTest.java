package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.external.PedidoServiceClient;
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
    private PedidoServiceClient pedidoServiceClient;
    private RealizarPagamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        gateway = mock(PagamentoGateway.class);
        pedidoServiceClient = mock(PedidoServiceClient.class);
        useCase = new RealizarPagamentoUseCase(gateway, pedidoServiceClient);
    }

    @Test
    void deveProcessarPagamentoComSucesso() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, pedidoId, "12345678901", new BigDecimal("100.00"), StatusPagamento.PENDENTE, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        assertNotNull(response);
        assertEquals(StatusPagamento.SUCESSO.name(), response.status());
        //assertEquals("PROCESSADO_SUCESSO", response.status());
        verify(pedidoServiceClient).atualizarStatusPedido(eq(pedidoId), eq("PROCESSADO_SUCESSO"));
    }

    @Test
    void deveProcessarPagamentoSemCredito() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, pedidoId, "12345678902", new BigDecimal("80.00"), StatusPagamento.PENDENTE, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        assertEquals(StatusPagamento.FALHA_CARTAO.name(), response.status());
        //assertEquals("PROCESSADO_SEM_CREDITO", response.status());
        verify(pedidoServiceClient).atualizarStatusPedido(eq(pedidoId), eq("PROCESSADO_SEM_CREDITO"));
    }

    @Test
    void deveProcessarPagamentoComErroGenerico() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, pedidoId, "12345678900", new BigDecimal("50.00"), StatusPagamento.PENDENTE, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        assertEquals(StatusPagamento.FALHA_OUTROS.name(), response.status());
        //assertEquals("PROCESSADO_ERRO", response.status());
        verify(pedidoServiceClient).atualizarStatusPedido(eq(pedidoId), eq("PROCESSADO_ERRO"));
    }

    @Test
    void deveLancarExcecaoSePagamentoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(PagamentoNaoEncontradoException.class, () -> useCase.executar(id));
        verifyNoInteractions(pedidoServiceClient);
    }
}
