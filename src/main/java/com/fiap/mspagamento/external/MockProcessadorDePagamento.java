package com.fiap.mspagamento.external;

import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.springframework.stereotype.Component;

@Component
public class MockProcessadorDePagamento {

    public StatusPagamento processar(String numeroCartao) {
        if (numeroCartao.endsWith("1")) {
            return StatusPagamento.SUCESSO;
        } else if (numeroCartao.endsWith("2")) {
            return StatusPagamento.FALHA_CARTAO;
        } else {
            return StatusPagamento.FALHA_OUTROS;
        }
    }
}
