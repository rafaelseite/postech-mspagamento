package com.fiap.mspagamento.controller;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.usecases.CriarPagamentoUseCase;
import com.fiap.mspagamento.usecases.RealizarPagamentoUseCase;
import com.fiap.mspagamento.usecases.BuscarPagamentoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final CriarPagamentoUseCase criarPagamentoUseCase;
    private final RealizarPagamentoUseCase realizarPagamentoUseCase;
    private final BuscarPagamentoUseCase buscarPagamentoUseCase;

    public PagamentoController(CriarPagamentoUseCase criarPagamentoUseCase,
                               RealizarPagamentoUseCase realizarPagamentoUseCase,
                               BuscarPagamentoUseCase buscarPagamentoUseCase) {
        this.criarPagamentoUseCase = criarPagamentoUseCase;
        this.realizarPagamentoUseCase = realizarPagamentoUseCase;
        this.buscarPagamentoUseCase = buscarPagamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponse> criar(@Valid @RequestBody PagamentoRequest request) {
        var criado = criarPagamentoUseCase.executar(request);
        return ResponseEntity.ok(PagamentoMapper.toResponse(criado));
    }



    @PostMapping("/{id}/processar")
    public ResponseEntity<PagamentoResponse> processar(@PathVariable UUID id) {
        var processado = realizarPagamentoUseCase.executar(id);
        return ResponseEntity.ok(processado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable UUID id) {
        return buscarPagamentoUseCase.buscarPorId(id)
                .map(p -> ResponseEntity.ok(PagamentoMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }
}
