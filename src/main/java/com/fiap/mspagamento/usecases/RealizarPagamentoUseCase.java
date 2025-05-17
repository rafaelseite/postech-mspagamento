package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.FalhaAoReporEstoqueException;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.external.EstoqueServiceClient;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RealizarPagamentoUseCase {

    private final PagamentoGateway gateway;
    private final EstoqueServiceClient estoqueServiceClient;

    public RealizarPagamentoUseCase(PagamentoGateway gateway, EstoqueServiceClient estoqueServiceClient) {
        this.gateway = gateway;
        this.estoqueServiceClient = estoqueServiceClient;
    }

    public PagamentoResponse executar(UUID idPagamento) {
        Pagamento pagamento = gateway.buscarPorId(idPagamento)
                .orElseThrow(() -> new PagamentoNaoEncontradoException(idPagamento));

        StatusPagamento status = processarCartao(pagamento.getNumeroCartao());
        pagamento.setStatus(status);

        if (status != StatusPagamento.SUCESSO) {
            try {
                estoqueServiceClient.devolverEstoque(pagamento.getSku(), pagamento.getQuantidade());
            } catch (Exception e) {
                throw new FalhaAoReporEstoqueException(pagamento.getSku());
            }
        }

        Pagamento atualizado = gateway.salvar(pagamento);
        return PagamentoMapper.toResponse(atualizado);
    }

    private StatusPagamento processarCartao(String numeroCartao) {
        if (numeroCartao.endsWith("1")) return StatusPagamento.SUCESSO;
        if (numeroCartao.endsWith("2")) return StatusPagamento.FALHA_CARTAO;
        return StatusPagamento.FALHA_OUTROS;
    }
}
