package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.valueobjects.Pagamento;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarPagamentoUseCase {

    private final PagamentoGateway gateway;

    public BuscarPagamentoUseCase(PagamentoGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<Pagamento> buscarPorId(UUID id) {
        return gateway.buscarPorId(id);
    }
}
