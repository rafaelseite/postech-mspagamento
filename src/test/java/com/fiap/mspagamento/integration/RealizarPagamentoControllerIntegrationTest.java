package com.fiap.mspagamento.integration;

import com.fiap.mspagamento.dto.PagamentoRequest;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fiap.mspagamento.external.EstoqueServiceClient;
import com.fiap.mspagamento.dto.PagamentoResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"spring.profiles.active=test"})
class RealizarPagamentoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PagamentoRepository repository;

    @MockBean
    private EstoqueServiceClient estoqueServiceClient;

    private UUID pedidoId;
    private UUID pagamentoId;

    @BeforeEach
    void setUp() {
        pedidoId = UUID.randomUUID();
        pagamentoId = UUID.randomUUID();

        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(pagamentoId);
        entity.setPedidoId(pedidoId);
        entity.setNumeroCartao("99999999992"); // termina com 2 => falha_cartao
        entity.setValor(new BigDecimal("99.99"));
        entity.setStatus(StatusPagamento.PENDENTE.name());
        entity.setCriadoEm(LocalDateTime.now());

        repository.save(entity);
    }

    @Test
    void deveProcessarPagamentoEPossivelmenteNotificarEstoque() {
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

        if (body.status().equals(StatusPagamento.FALHA_CARTAO.name()) ||
                body.status().equals(StatusPagamento.FALHA_OUTROS.name()) ||
                body.status().equals(StatusPagamento.FALHA_SISTEMA.name())) {

            verify(estoqueServiceClient, times(1)).devolverEstoque(pedidoId.toString());
        } else {
            verify(estoqueServiceClient, never()).devolverEstoque(any());
        }
    }
}
