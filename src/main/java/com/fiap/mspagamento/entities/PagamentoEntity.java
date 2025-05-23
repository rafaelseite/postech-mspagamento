package com.fiap.mspagamento.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
public class PagamentoEntity {

    @Id
    private UUID id;

    @Column(name = "pedido_id", nullable = false)
    private UUID pedidoId;

    @Column(name = "numero_cartao", nullable = false)
    private String numeroCartao;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private String status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPedidoId() { return pedidoId; }
    public void setPedidoId(UUID pedidoId) { this.pedidoId = pedidoId; }

    public String getNumeroCartao() { return numeroCartao; }
    public void setNumeroCartao(String numeroCartao) { this.numeroCartao = numeroCartao; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
