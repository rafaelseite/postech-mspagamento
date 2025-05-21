package com.fiap.mspagamento.external;

import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MockProcessadorDePagamentoTest {

    @Autowired
    private MockProcessadorDePagamento processador;

    @ParameterizedTest
    @CsvSource({
            "1111, SUCESSO",
            "2222, FALHA_CARTAO",
            "3333, FALHA_OUTROS",
            "1234, FALHA_OUTROS"
    })
    void deveRetornarStatusEsperadoConformeFinalDoCartao(String numeroCartao, StatusPagamento esperado) {
        StatusPagamento status = processador.processar(numeroCartao);
        assertEquals(esperado, status);
    }
}
