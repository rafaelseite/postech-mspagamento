package com.fiap.mspagamento.interfaces;

import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MockPagamentoAdapter {

    private final Random random = new Random();

    public StatusPagamento processarPagamento(String numeroCartao, Double valor) {
        int chance = random.nextInt(100);

        if (chance < 70) {
            return StatusPagamento.SUCESSO;
        } else if (chance < 85) {
            return StatusPagamento.FALHA_CARTAO;
        } else {
            return StatusPagamento.FALHA_OUTROS;
        }
    }
}
