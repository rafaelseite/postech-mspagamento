package com.fiap.mspagamento.exception;

public class FalhaAoReporEstoqueException extends RuntimeException {
    public FalhaAoReporEstoqueException(String pedidoId) {
        super("Não foi possível devolver o estoque para o pedido: " + pedidoId);
    }
}
