package com.fiap.mspagamento.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public class PagamentoRequest {

    @NotNull(message = "PedidoId é obrigatório")
    private UUID pedidoId;

    @NotBlank(message = "SKU é obrigatório")
    private String sku;

    @Min(value = 1, message = "Quantidade deve ser maior ou igual a 1")
    private int quantidade;

    @NotBlank(message = "Número do cartão é obrigatório")
    private String numeroCartao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser positivo")
    private BigDecimal valor;


    public UUID getPedidoId() { return pedidoId; }
    public void setPedidoId(UUID pedidoId) { this.pedidoId = pedidoId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getNumeroCartao() { return numeroCartao; }
    public void setNumeroCartao(String numeroCartao) { this.numeroCartao = numeroCartao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
