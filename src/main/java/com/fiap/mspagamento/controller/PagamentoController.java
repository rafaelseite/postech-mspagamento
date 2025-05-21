package com.fiap.mspagamento.controller;

import com.fiap.mspagamento.dto.PagamentoRequest;
import com.fiap.mspagamento.dto.PagamentoResponse;
import com.fiap.mspagamento.interfaces.PagamentoMapper;
import com.fiap.mspagamento.usecases.BuscarPagamentoUseCase;
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
    private final BuscarPagamentoUseCase buscarPagamentoUseCase;

    public PagamentoController(CriarPagamentoUseCase criarPagamentoUseCase, RealizarPagamentoUseCase realizarPagamentoUseCase, BuscarPagamentoUseCase buscarPagamentoUseCase) {
        this.criarPagamentoUseCase = criarPagamentoUseCase;
        this.realizarPagamentoUseCase = realizarPagamentoUseCase;
        this.buscarPagamentoUseCase = buscarPagamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponse> criar(@Valid @RequestBody PagamentoRequest request) {
        try {
            var pagamento = criarPagamentoUseCase.executar(request);
            return ResponseEntity.ok(PagamentoMapper.toResponse(pagamento));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    /*@PostMapping
    public ResponseEntity<PagamentoResponse> criar(@Valid @RequestBody PagamentoRequest request) {
        var pagamento = criarPagamentoUseCase.executar(request);
        return ResponseEntity.ok(PagamentoMapper.toResponse(pagamento));
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable UUID id) {
        try {
            PagamentoResponse response = buscarPagamentoUseCase.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }




    @PostMapping("/{id}/processar")
    public ResponseEntity<PagamentoResponse> processar(@PathVariable UUID id) {
        return ResponseEntity.ok(realizarPagamentoUseCase.executar(id));
    }
}
