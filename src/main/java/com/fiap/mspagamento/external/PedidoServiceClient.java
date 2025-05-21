package com.fiap.mspagamento.external;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class PedidoServiceClient {

    private final RestTemplate restTemplate;

    public PedidoServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void atualizarStatusPedido(UUID pedidoId, String status) {
        String url = "http://mspedido:8083/api/atualizacao-pagamento";

        Map<String, Object> body = new HashMap<>();
        body.put("pedidoId", pedidoId);
        body.put("status", status);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, Void.class);
    }
}

