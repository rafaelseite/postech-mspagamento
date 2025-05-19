package com.fiap.mspagamento.integration;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CriarPagamentoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void deveCriarPagamentoComSucesso() {
        PagamentoRequest request = new PagamentoRequest();
        request.setPedidoId(UUID.randomUUID());
        request.setNumeroCartao("12345678901");
        request.setValorTotal(new BigDecimal("50.00"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PagamentoRequest> httpRequest = new HttpEntity<>(request, headers);

        ResponseEntity<PagamentoResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/pagamentos",
                httpRequest,
                PagamentoResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(StatusPagamento.PENDENTE.name());
    }
}
