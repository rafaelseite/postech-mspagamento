package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CriarPagamentoUseCase {

    private final PagamentoGateway gateway;

    public CriarPagamentoUseCase(PagamentoGateway gateway) {
        this.gateway = gateway;
    }

    public PagamentoResponse executar(PagamentoRequest request) {
        Pagamento pagamento = PagamentoMapper.toValueObject(request);
        Pagamento salvo = gateway.salvar(pagamento);
        return PagamentoMapper.toResponse(salvo);
    }


}
