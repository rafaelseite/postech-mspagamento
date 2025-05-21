package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.exception.PagamentoNaoEncontradoException;
import com.fiap.mspagamento.external.PedidoServiceClient;
import com.fiap.mspagamento.interfaces.PagamentoGateway;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.valueobjects.Pagamento;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RealizarPagamentoUseCase {

    private final PagamentoGateway gateway;
    private final PedidoServiceClient pedidoServiceClient;

    public RealizarPagamentoUseCase(PagamentoGateway gateway, PedidoServiceClient pedidoServiceClient) {
        this.gateway = gateway;
        this.pedidoServiceClient = pedidoServiceClient;
    }

    public PagamentoResponse executar(UUID idPagamento) {
        Pagamento pagamento = gateway.buscarPorId(idPagamento)
                .orElseThrow(() -> new PagamentoNaoEncontradoException(idPagamento));

        StatusPagamento status = processarCartao(pagamento.getNumeroCartao());
        pagamento.setStatus(status);

        Pagamento atualizado = gateway.salvar(pagamento);

        String statusPedido = mapearStatusParaPedido(status);

        pedidoServiceClient.atualizarStatusPedido(pagamento.getPedidoId(), statusPedido);

        return PagamentoMapper.toResponse(atualizado);
    }

    private StatusPagamento processarCartao(String numeroCartao) {
        if (numeroCartao.endsWith("1")) return StatusPagamento.SUCESSO;
        if (numeroCartao.endsWith("2")) return StatusPagamento.FALHA_CARTAO;
        return StatusPagamento.FALHA_OUTROS;
    }

    private String mapearStatusParaPedido(StatusPagamento status) {
        return switch (status) {
            case SUCESSO -> "PROCESSADO_SUCESSO";
            case FALHA_CARTAO -> "PROCESSADO_SEM_CREDITO";
            default -> "PROCESSADO_ERRO";
        };
    }
}
