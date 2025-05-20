# Order Received API
Essa api é parte do projeto da **Fase 4** da Especialização em Arquitetura e Desenvolvimento Java da FIAP.
Um sistema de gerenciamento de pedidos integrado com Spring e Mocrosserviços. A aplicação foi desenvolvida em **Java 21**,
utilizando **Spring Boot**, **Maven**, um banco de dados **H2** para testes, **Mockito** e **JUnit 5** para testes
unitários, **Lombok** para facilitar o desenvolvimento e documentação gerada pelo **Swagger**.

## Descrição do Projeto
O objetivo desse sistema é abranger desde a gestão de clientes e produtos até o processamento e entrega de pedidos,
enfatizando a autonomia dos serviços, comunicação eficaz e persistência de dados isolada. Esta API é responsável pelo
recebimento dos pedidos e enfileiramento nas filas.

## Funcionalidades
A API permite:
- **Receber** os pedidos realizados pelo cliente e os enfileirar nas filas de processamento.

## Tecnologias Utilizadas
- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Maven**
- **Banco de Dados H2**
- **Banco de Dados Mysql**
- **Mockito** e **JUnit 5**
- **Lombok**
- **Swagger**
- **Docker Compose**
- **Spotless**
- **Jacoco**
- **Docker**
- **RabbitMQ**

## Estrutura do Projeto
O projeto segue uma arquitetura modularizada, organizada nas seguintes camadas:
- `config`: Configurações do RabbitMQ.
- `core`: Contém as regras de negócio do sistema.
- `core.dto`: Representa as entradas e saídas de dados para a API.
- `core.gateway`: Interfaces para interação com o banco de dados.
- `entrypoint.controller`: Controladores responsáveis por expor os endpoints da API.
- `infrastructure.gateway.queue`: Implementações das interfaces de gateway para o RabbitMQ.

## Pré-requisitos
- Java 21
- Maven 3.6+
- IDE como IntelliJ IDEA ou Eclipse

## Configuração e Execução
1. **Clone o repositório**:
   ```bash
   git clone https://github.com/GabiFerraz/OrderReceived-Api.git
   ```
2. **Instale as dependências:**
   ```bash
   mvn clean install
   ```
3. **Execute o projeto:**
   ```bash
   mvn spring-boot:run
   ```

## Uso da API
Para funcionamento, _subir o docker-compose: **docker-compose up --build**

Os endpoints estão documentados via **Swagger**:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Swagger JSON**: http://localhost:8080/v3/api-docs

### Possibilidades de Chamadas da API
1. **Cadastro de Cliente:**
```json
curl --location 'localhost:8083/api/order-received' \
--header 'Content-Type: application/json' \
--data '{
"productSku": "BOLA-123-ABC",
"productQuantity": 10,
"clientCpf": "12345678901",
"paymentMethod": "CREDIT_CARD",
"cardNumber": "1234567890123456"
}'
```

## Testes
Para rodar os testes unitários:
```bash
mvn test
```

**Rodar o coverage:**
   ```bash
   mvn clean package
   ```
Depois acessar pasta target/site/jacoco/index.html

O projeto inclui testes unitários, testes de integração e testes de arquitetura para garantir a qualidade e
confiabilidade da API.

## Desenvolvedora:
- **Gabriela de Mesquita Ferraz** - RM: 358745