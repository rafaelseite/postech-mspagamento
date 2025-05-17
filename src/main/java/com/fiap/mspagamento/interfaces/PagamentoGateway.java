package com.fiap.mspagamento.interfaces;

import com.fiap.mspagamento.valueobjects.Pagamento;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoGateway {

    Pagamento salvar(Pagamento pagamento);

    Optional<Pagamento> buscarPorId(UUID id);
}
