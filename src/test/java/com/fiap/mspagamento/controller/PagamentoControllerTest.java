package com.fiap.mspagamento.controller;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.usecases.BuscarPagamentoUseCase;
import com.fiap.mspagamento.usecases.CriarPagamentoUseCase;
import com.fiap.mspagamento.usecases.RealizarPagamentoUseCase;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoControllerTest {

    private CriarPagamentoUseCase criarUseCase;
    private RealizarPagamentoUseCase realizarUseCase;
    private BuscarPagamentoUseCase buscarUseCase;
    private PagamentoController controller;

    @BeforeEach
    void setUp() {
        criarUseCase = mock(CriarPagamentoUseCase.class);
        realizarUseCase = mock(RealizarPagamentoUseCase.class);
        buscarUseCase = mock(BuscarPagamentoUseCase.class);
        controller = new PagamentoController(criarUseCase, realizarUseCase, buscarUseCase);
    }

    @Test
    void deveCriarPagamento() {
        PagamentoRequest request = new PagamentoRequest();
        request.setPedidoId(UUID.randomUUID());
        request.setNumeroCartao("12345678901");
        request.setValor(new BigDecimal("100.00"));

        Pagamento pagamentoCriado = new Pagamento(
                UUID.randomUUID(),
                request.getPedidoId(),
                request.getNumeroCartao(),
                request.getValor(),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        when(criarUseCase.executar(any())).thenReturn(pagamentoCriado);

        ResponseEntity<PagamentoResponse> response = controller.criar(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pagamentoCriado.getPedidoId(), response.getBody().pedidoId());
        assertEquals(pagamentoCriado.getStatus().name(), response.getBody().status());

        verify(criarUseCase).executar(any());
    }

    @Test
    void deveProcessarPagamento() {
        UUID id = UUID.randomUUID();
        PagamentoResponse responseMock = new PagamentoResponse(
                id,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                StatusPagamento.SUCESSO.name(),
                LocalDateTime.now()
        );

        when(realizarUseCase.executar(id)).thenReturn(responseMock);

        ResponseEntity<PagamentoResponse> response = controller.processar(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseMock, response.getBody());
        verify(realizarUseCase).executar(id);
    }

    @Test
    void deveBuscarPagamentoExistente() {
        UUID id = UUID.randomUUID();
        Pagamento pagamento = new Pagamento(
                id,
                UUID.randomUUID(),
                "12345678901",
                new BigDecimal("100.00"),
                StatusPagamento.SUCESSO,
                LocalDateTime.now()
        );

        when(buscarUseCase.buscarPorId(id)).thenReturn(Optional.of(pagamento));

        ResponseEntity<PagamentoResponse> response = controller.buscar(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(pagamento.getId(), response.getBody().id());
        verify(buscarUseCase).buscarPorId(id);
    }

    @Test
    void deveRetornarNotFoundQuandoNaoExistir() {
        UUID id = UUID.randomUUID();

        when(buscarUseCase.buscarPorId(id)).thenReturn(Optional.empty());

        ResponseEntity<PagamentoResponse> response = controller.buscar(id);

        assertEquals(404, response.getStatusCodeValue());
        verify(buscarUseCase).buscarPorId(id);
    }
}
