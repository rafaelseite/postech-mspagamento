package com.fiap.mspagamento.controller;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.mspagamento.usecases.BuscarPagamentoUseCase;
import com.fiap.mspagamento.usecases.RealizarPagamentoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fiap.mspagamento.usecases.CriarPagamentoUseCase;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CriarPagamentoUseCase criarPagamentoUseCase;

    @MockBean
    private RealizarPagamentoUseCase realizarPagamentoUseCase;



    @Test
    void deveRetornarBadRequestQuandoRequestInvalido() throws Exception {
        PagamentoRequest requestInvalido = new PagamentoRequest();


        mockMvc.perform(post("/pagamentos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }
}
