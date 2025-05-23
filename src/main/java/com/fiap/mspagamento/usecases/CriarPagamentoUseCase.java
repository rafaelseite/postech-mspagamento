package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.valueobjects.Pagamento;
import org.springframework.stereotype.Service;

@Service
public class CriarPagamentoUseCase {

    private final PagamentoGateway gateway;

    public CriarPagamentoUseCase(PagamentoGateway gateway) {
        this.gateway = gateway;
    }

    public Pagamento executar(PagamentoRequest request) {
        Pagamento pagamento = PagamentoMapper.toValueObject(request);
        return gateway.salvar(pagamento);
    }
}
