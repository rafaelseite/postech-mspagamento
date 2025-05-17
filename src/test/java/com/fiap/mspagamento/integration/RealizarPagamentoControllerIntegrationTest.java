package com.fiap.mspagamento.integration;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.entities.PagamentoEntity;
import com.fiap.mspagamento.gateways.database.jpa.PagamentoRepository;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.fiap.mspagamento.external.PedidoServiceClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RealizarPagamentoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PagamentoRepository repository;

    @MockBean
    private PedidoServiceClient pedidoServiceClient;

    private UUID pagamentoId;
    private UUID pedidoId;

    @BeforeEach
    void setUp() {
        pagamentoId = UUID.randomUUID();
        pedidoId = UUID.randomUUID();

        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(pagamentoId);
        entity.setPedidoId(pedidoId);
        entity.setNumeroCartao("12345678902"); // termina com 2 (simula falha)
        entity.setValor(new BigDecimal("150.00"));
        entity.setStatus(StatusPagamento.PENDENTE.name());
        entity.setCriadoEm(LocalDateTime.now());

        repository.save(entity);
    }

    @Test
    void deveProcessarPagamentoEAtualizarPedido() {
        String url = "http://localhost:" + port + "/pagamentos/" + pagamentoId + "/processar";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PagamentoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                PagamentoResponse.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        PagamentoResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isEqualTo(pagamentoId);

        // Verifica que o serviço de pedido foi chamado
        verify(pedidoServiceClient).atualizarStatusPedido(eq(pedidoId), any());
    }
}
