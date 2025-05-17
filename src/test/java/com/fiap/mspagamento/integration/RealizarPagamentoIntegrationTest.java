package com.fiap.mspagamento.integration;

import com.fiap.mspagamento.entities.PagamentoEntity;
import com.fiap.mspagamento.gateways.database.jpa.PagamentoRepository;
import com.fiap.mspagamento.valueobjects.StatusPagamento;
import com.fiap.mspagamento.external.EstoqueServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {"spring.profiles.active=test"})
class RealizarPagamentoIntegrationTest {

    @Autowired
    private PagamentoRepository repository;

    @MockBean
    private EstoqueServiceClient estoqueServiceClient;

    private UUID pedidoId;

    @BeforeEach
    void setUp() {
        pedidoId = UUID.randomUUID();

        PagamentoEntity entity = new PagamentoEntity();
        entity.setId(UUID.randomUUID());
        entity.setPedidoId(pedidoId);
        entity.setNumeroCartao("99999999992"); // termina com 2 → erro
        entity.setValor(new BigDecimal("150.00"));
        entity.setStatus(StatusPagamento.PENDENTE.name());
        entity.setCriadoEm(LocalDateTime.now());

        repository.save(entity);
    }

    @Test
    void deveDevolverEstoqueQuandoPagamentoFalhar() {
        var pagamento = repository.findAll().get(0);


        pagamento.setStatus(StatusPagamento.FALHA_CARTAO.name());
        repository.save(pagamento);

        estoqueServiceClient.devolverEstoque(pagamento.getPedidoId().toString());

        verify(estoqueServiceClient, times(1)).devolverEstoque(pagamento.getPedidoId().toString());

        var atualizado = repository.findById(pagamento.getId()).orElseThrow();
        assertThat(atualizado.getStatus()).isEqualTo(StatusPagamento.FALHA_CARTAO.name());
    }
}
