package com.fiap.mspagamento.usecases;

import com.fiap.mspagamento.interfaces.PagamentoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CriarPagamentoUseCase criarPagamentoUseCase(PagamentoGateway gateway) {
        return new CriarPagamentoUseCase(gateway);
    }
}
