package com.fiap.mspagamento.interfaces;

import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MockPagamentoAdapterTest {

    private MockPagamentoAdapter adapter;

    @BeforeEach
    void setup() throws Exception {
        adapter = new MockPagamentoAdapter();
        Field randomField = MockPagamentoAdapter.class.getDeclaredField("random");
        randomField.setAccessible(true);
        randomField.set(adapter, new Random(0));
    }

    @RepeatedTest(10)
    void deveRetornarStatusValido() {
        StatusPagamento status = adapter.processarPagamento("1234567890123456", 100.0);
        assertTrue(
                status == StatusPagamento.SUCESSO ||
                        status == StatusPagamento.FALHA_CARTAO ||
                        status == StatusPagamento.FALHA_OUTROS
        );
    }
}
