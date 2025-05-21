# 📦 Microsserviço de Pagamento – FIAP Tech Challenge 4 

Microsserviço responsável pelo processamento de pagamentos da aplicação Tech Challenge 4 FIAP - Pós Tech.

## 🚀 Tecnologias

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Flyway
- Docker & Docker Compose
- REST API
- Testes com JUnit
- JaCoCo (Cobertura de Testes 80%+)
- Integração com serviços externos

## 📁 Arquitetura
```
com.fiap.mspagamento
├── config
├── controller
├── dto
├── entities
├── exception
├── external
├── gateways.database.jpa
├── interfaces
├── usecases
├── valueobjects
└── tests
```

## ⚙️ Como Rodar

### 1. Pré-requisitos

- Docker e Docker Compose
- JDK 21 instalado
- Maven instalado (caso deseje buildar fora do Docker)

### 2. Build da Imagem

```bash
docker build -t rseite/app-pagamento:2.1 .
```

### 3. Subir todo o ecossistema

Vá até o repositório do Docker Compose principal (ex: `postech-docker-compose`) e execute:

```bash
docker compose up -d
```

Certifique-se de que o container `mspagamento` esteja rodando:

```bash
docker ps
```

### 4. Testar Endpoint

POST de pagamento:
```http
POST http://localhost:8085/pagamentos
Content-Type: application/json

{
  "pedidoId": "uuid-do-pedido",
  "numeroCartao": "4111111111111111",
  "valorTotal": 99.90
}
```

## 🧪 Testes

Execute os testes com:

```bash
mvn test "-Dspring.profiles.active=test"
```

## 🛠️ Principais Funcionalidades

- Processar pagamento de pedidos
- Atualizar status do pedido via integração com `mspedido`
- Armazenar histórico de pagamentos com status (`PENDENTE`, `SUCESSO`, `FALHA_*`)

## 🔄 Integrações

- `PedidoServiceClient`: responsável por comunicar o status do pagamento para `mspedido`
