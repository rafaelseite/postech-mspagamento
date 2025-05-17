package com.fiap.mspagamento.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pagamento {

    private UUID id;
    private UUID pedidoId;
    private String sku;
    private int quantidade;
    private String numeroCartao;
    private BigDecimal valor;
    private StatusPagamento status;
    private LocalDateTime criadoEm;

    public Pagamento(UUID id, UUID pedidoId, String sku, int quantidade, String numeroCartao, BigDecimal valor, StatusPagamento status, LocalDateTime criadoEm) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.sku = sku;
        this.quantidade = quantidade;
        this.numeroCartao = numeroCartao;
        this.valor = valor;
        this.status = status;
        this.criadoEm = criadoEm;
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
