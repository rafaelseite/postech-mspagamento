package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.external.PedidoServiceClient;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        verify(pedidoServiceClient).atualizarStatusPedido(idCaptor.capture(), statusCaptor.capture());

        System.out.println("[SUCESSO] ID enviado: " + idCaptor.getValue());
        System.out.println("[SUCESSO] Status enviado: " + statusCaptor.getValue());

        assertNotNull(response);
        assertEquals("PROCESSADO_SUCESSO", statusCaptor.getValue());
    }

    @Test
    void deveMapearStatusCorretamenteParaPagamentoResponse() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();

        Pagamento sucesso = new Pagamento(id, pedidoId, "12345678901", new BigDecimal("100.00"), StatusPagamento.SUCESSO, LocalDateTime.now());
        Pagamento falhaCartao = new Pagamento(id, pedidoId, "12345678902", new BigDecimal("100.00"), StatusPagamento.FALHA_CARTAO, LocalDateTime.now());
        Pagamento falhaOutros = new Pagamento(id, pedidoId, "12345678900", new BigDecimal("100.00"), StatusPagamento.FALHA_OUTROS, LocalDateTime.now());

        assertEquals("PROCESSADO_SUCESSO", PagamentoResponse.fromEntity(sucesso).status());
        assertEquals("PROCESSADO_SEM_CREDITO", PagamentoResponse.fromEntity(falhaCartao).status());
        assertEquals("PROCESSADO_ERRO", PagamentoResponse.fromEntity(falhaOutros).status());
    }

    @Test
    void deveProcessarPagamentoSemCredito() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, pedidoId, "12345678902", new BigDecimal("80.00"), StatusPagamento.PENDENTE, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        verify(pedidoServiceClient).atualizarStatusPedido(idCaptor.capture(), statusCaptor.capture());

        System.out.println("[SEM_CREDITO] ID enviado: " + idCaptor.getValue());
        System.out.println("[SEM_CREDITO] Status enviado: " + statusCaptor.getValue());

        assertEquals("PROCESSADO_SEM_CREDITO", statusCaptor.getValue());
    }

    @Test
    void deveProcessarPagamentoComErroGenerico() {
        UUID id = UUID.randomUUID();
        UUID pedidoId = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(id, pedidoId, "12345678900", new BigDecimal("50.00"), StatusPagamento.PENDENTE, LocalDateTime.now());

        when(gateway.buscarPorId(id)).thenReturn(Optional.of(pagamento));
        when(gateway.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponse response = useCase.executar(id);

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> statusCaptor = ArgumentCaptor.forClass(String.class);
        verify(pedidoServiceClient).atualizarStatusPedido(idCaptor.capture(), statusCaptor.capture());

        System.out.println("[ERRO] ID enviado: " + idCaptor.getValue());
        System.out.println("[ERRO] Status enviado: " + statusCaptor.getValue());

        assertEquals("PROCESSADO_ERRO", statusCaptor.getValue());
    }

    @Test
    void deveLancarExcecaoSePagamentoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(gateway.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(PagamentoNaoEncontradoException.class, () -> useCase.executar(id));
        verifyNoInteractions(pedidoServiceClient);
    }
}
