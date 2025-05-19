package com.fiap.mspagamento.gateways.database.jpa;

import com.fiap.mspagamento.entities.PagamentoEntity;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PagamentoGatewayImpl implements PagamentoGateway {

    private final PagamentoRepository repository;

    public PagamentoGatewayImpl(PagamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        PagamentoEntity entity = toEntity(pagamento);
        PagamentoEntity salvo = repository.save(entity);
        return toDomain(salvo);
    }

    @Override
    public Optional<Pagamento> buscarPorId(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    private PagamentoEntity toEntity(Pagamento pagamento) {
        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(pagamento.getId());
        entity.setPedidoId(pagamento.getPedidoId());
        entity.setNumeroCartao(pagamento.getNumeroCartao());
        entity.setValorTotal(pagamento.getValorTotal());
        entity.setStatus(pagamento.getStatus().name());
        entity.setCriadoEm(pagamento.getCriadoEm());
        return entity;
    }

    private Pagamento toDomain(PagamentoEntity entity) {
        return new Pagamento(
                entity.getId(),
                entity.getPedidoId(),
                entity.getNumeroCartao(),
                entity.getValorTotal(),
                StatusPagamento.valueOf(entity.getStatus()),
                entity.getCriadoEm()
        );
    }
}
