package com.fiap.mspagamento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.usecases.BuscarPagamentoUseCase;
import com.fiap.mspagamento.usecases.CriarPagamentoUseCase;
import com.fiap.mspagamento.usecases.RealizarPagamentoUseCase;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CriarPagamentoUseCase criarPagamentoUseCase;

    @MockBean
    private RealizarPagamentoUseCase realizarPagamentoUseCase;

    @MockBean
    private BuscarPagamentoUseCase buscarPagamentoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private PagamentoRequest request;
    private Pagamento pagamento;
    private PagamentoResponse pagamentoResponse;
    private UUID pagamentoId;

    @BeforeEach
    void setUp() {
        pagamentoId = UUID.randomUUID();

        request = new PagamentoRequest();
        request.setPedidoId(UUID.randomUUID());
        request.setNumeroCartao("12345678901");
        request.setValorTotal(new BigDecimal("150.00"));

        pagamento = new Pagamento(
                pagamentoId,
                request.getPedidoId(),
                request.getNumeroCartao(),
                request.getValorTotal(),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        pagamentoResponse = new PagamentoResponse(
                pagamentoId,
                request.getPedidoId(),
                request.getValorTotal(),
                StatusPagamento.PENDENTE.name(),
                LocalDateTime.now()
        );
    }

    @Test
    void deveCriarPagamento() throws Exception {
        when(criarPagamentoUseCase.executar(any())).thenReturn(pagamento);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deveProcessarPagamentoComSucesso() throws Exception {
        when(realizarPagamentoUseCase.executar(pagamentoId)).thenReturn(pagamentoResponse);

        mockMvc.perform(post("/pagamentos/" + pagamentoId + "/processar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pagamentoResponse.id().toString()))
                .andExpect(jsonPath("$.status").value(pagamentoResponse.status()));
    }

    @Test
    void deveRetornarErro500AoCriarPagamentoQuandoOcorreExcecao() throws Exception {
        when(criarPagamentoUseCase.executar(any())).thenThrow(new RuntimeException("Erro interno"));

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deveRetornar404QuandoPagamentoNaoEncontrado() throws Exception {
        when(buscarPagamentoUseCase.buscarPorId(pagamentoId)).thenThrow(new RuntimeException("Não encontrado"));

        mockMvc.perform(get("/pagamentos/" + pagamentoId))
                .andExpect(status().isNotFound());
    }



    @Test
    void deveBuscarPagamentoPorIdComSucesso() throws Exception {
        when(buscarPagamentoUseCase.buscarPorId(pagamentoId)).thenReturn(pagamentoResponse);

        mockMvc.perform(get("/pagamentos/" + pagamentoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pagamentoResponse.id().toString()))
                .andExpect(jsonPath("$.pedidoId").value(pagamentoResponse.pedidoId().toString()))
                .andExpect(jsonPath("$.status").value(pagamentoResponse.status()))
                .andExpect(jsonPath("$.valorTotal").value(pagamentoResponse.valorTotal().doubleValue()));
    }
}
