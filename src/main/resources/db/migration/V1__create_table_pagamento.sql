CREATE TABLE pagamento (
    id UUID PRIMARY KEY,
    pedido_id UUID NOT NULL,
    numero_cartao VARCHAR(255) NOT NULL,
    valor_total NUMERIC(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP NOT NULL
);
