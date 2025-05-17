package com.fiap.mspagamento.controller;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.usecases.CriarPagamentoUseCase;
import com.fiap.mspagamento.usecases.RealizarPagamentoUseCase;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CriarPagamentoUseCase criarPagamentoUseCase;

    @MockBean
    private RealizarPagamentoUseCase realizarPagamentoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private PagamentoRequest request;

    @BeforeEach
    void setUp() {
        request = new PagamentoRequest();
        request.setPedidoId(UUID.randomUUID());
        request.setNumeroCartao("12345678901");
        request.setValor(new BigDecimal("150.00"));
    }

    @Test
    void deveCriarPagamento() throws Exception {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                request.getPedidoId(),
                request.getNumeroCartao(),
                request.getValor(),
                StatusPagamento.PENDENTE,
                LocalDateTime.now()
        );

        when(criarPagamentoUseCase.executar(any())).thenReturn(pagamento);

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
