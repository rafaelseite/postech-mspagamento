package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarPagamentoUseCase {

    private final PagamentoGateway gateway;

    public BuscarPagamentoUseCase(PagamentoGateway gateway) {
        this.gateway = gateway;
    }

   public PagamentoResponse buscarPorId(UUID id) {
        return gateway.buscarPorId(id)
                .map(PagamentoMapper::toResponse)
                .orElseThrow(() -> new PagamentoNaoEncontradoException(id));
    }

    /*
    public PagamentoResponse buscarPorId(UUID id) {
        return gateway.buscarPorId(id)
                .map(p -> {
                    System.out.println("DEBUG - Pagamento recuperado: " + p);
                    return PagamentoMapper.toResponse(p);
                })
                .orElseThrow(() -> new PagamentoNaoEncontradoException(id));
    }*/

}
