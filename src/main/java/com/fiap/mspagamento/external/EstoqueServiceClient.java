package com.fiap.mspagamento.external;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EstoqueServiceClient {

    private final RestTemplate restTemplate;

    public EstoqueServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public void devolverEstoque(String sku, int quantidade) {
        String url = "http://localhost:8086/admin/adiciona-produtos-no-estoque?sku=" + sku + "&quantidade=" + quantidade;
        restTemplate.put(url, null);
    }
}
