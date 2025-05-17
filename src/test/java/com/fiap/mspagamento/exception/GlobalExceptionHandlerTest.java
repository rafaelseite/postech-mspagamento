package com.fiap.mspagamento.exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.profiles.active=test"})
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarBadRequestParaPagamentoNaoEncontrado() {
        PagamentoNaoEncontradoException ex = new PagamentoNaoEncontradoException(UUID.randomUUID());
        ResponseEntity<Object> response = handler.tratarPagamentoNaoEncontrado(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(Map.class);
    }

    @Test
    void deveRetornarErroInternoParaExceptionGenerica() {
        Exception ex = new Exception("Erro genérico");
        ResponseEntity<Object> response = handler.tratarErroInterno(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isInstanceOf(Map.class);
    }
}
