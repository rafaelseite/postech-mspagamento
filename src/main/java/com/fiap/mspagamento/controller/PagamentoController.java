package com.fiap.mspagamento.controller;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.usecases.CriarPagamentoUseCase;
import com.fiap.mspagamento.usecases.RealizarPagamentoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final CriarPagamentoUseCase criarPagamentoUseCase;
    private final RealizarPagamentoUseCase realizarPagamentoUseCase;

    public PagamentoController(CriarPagamentoUseCase criarPagamentoUseCase, RealizarPagamentoUseCase realizarPagamentoUseCase) {
        this.criarPagamentoUseCase = criarPagamentoUseCase;
        this.realizarPagamentoUseCase = realizarPagamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponse> criar(@Valid @RequestBody PagamentoRequest request) {
        var pagamento = criarPagamentoUseCase.executar(request);
        return ResponseEntity.ok(PagamentoMapper.toResponse(pagamento));
    }

    @PostMapping("/{id}/processar")
    public ResponseEntity<PagamentoResponse> processar(@PathVariable UUID id) {
        return ResponseEntity.ok(realizarPagamentoUseCase.executar(id));
    }
}
