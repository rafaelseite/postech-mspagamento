package com.fiap.mspagamento.exception;

import java.util.UUID;

public class PagamentoNaoEncontradoException extends RuntimeException {
    public PagamentoNaoEncontradoException(UUID id) {
        super("Pagamento com ID " + id + " não foi encontrado.");
    }
}
