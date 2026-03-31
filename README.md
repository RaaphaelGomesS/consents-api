# API de gerenciamento de consentimentos (Consents API)

Esta é uma API REST desenvolvida em Spring para o gerenciamento de consentimento de uso de dados de usuários. O sistema garante que cada usuário (identificado pelo CPF) possua apenas um registro de consentimento e aplica regras automatizadas de expiração. O projeto foi pensado como um microsserviço a ser integrado em um sistema que já possui um processo de autenticação.

## 🚀 Funcionalidades

* **Criação de consentimento:** Permite criar um novo consentimento vinculado a um CPF. Só é permitido um consentimento por CPF.
* **Validação de dados:** O CPF informado na requisição é validado rigorosamente através da anotação `@CPF` do Hibernate Validator, a validação leva tanto em conta a estrutura quanto faz um cálculo para checar a validade do CPF.
* **Expiração automática:** Os consentimentos têm uma validade arbitrária de 2 meses. A mudança de status para `EXPIRED` ocorre de duas formas:
    * No momento da consulta (se a data atual for maior que a data de expiração).
    * Através de um Job agendado (`@Scheduled`) que roda diariamente ao meio-dia para varrer e atualizar a base de dados.
* **Revogação:** É possível revogar um consentimento, atualizando seu status para `REVOKED` sem excluí-lo fisicamente do banco de dados.
* **Reativação:** Um consentimento revogado ou expirado pode ser reativado, atualizando sua validade.
* **Consultas:**
    * Busca de um consentimento específico pelo seu ID (UUID).
    * Busca paginada de consentimentos com filtros por status (`ACTIVE`, `REVOKED`, `EXPIRED`).

## 🛠️ Tecnologias utilizadas

* **Java 21**
* **Spring Boot (3.5.x)** (Web, Data MongoDB, Actuator, Validation)
* **MongoDB** (Banco de dados NoSQL)
* **Docker & Docker Compose** (Para conteinerização do banco de dados)
* **Swagger/OpenAPI (Springdoc)** (Para documentação da API)
* **Lombok** (Para redução de boilerplate)
* **JUnit 5, Mockito & Testcontainers** (Para testes unitários e de integração)
* **Spring Dotenv** (Para gerenciamento de variáveis de ambiente)

## 🚀 Como executar a aplicação

### 1. Configuração do ambiente (.env)
A aplicação utiliza um arquivo `.env` para gerenciar as credenciais do banco de dados de forma segura. Na raiz do projeto, crie um arquivo chamado `.env` (você pode se basear no conteúdo abaixo):

```env
MONGO_DB_USERNAME=admin
MONGO_DB_PASSWORD=secret
MONGO_DB_DATABASE=consents_db
```

### 2. Subindo o banco de dados (MongoDB)

Com o Docker em execução na sua máquina, utilize o docker compose na raiz do projeto para subir o container do MongoDB:

```bash
docker compose up -d
```

O banco estará disponível localmente na porta 27017.

### 3. Iniciando a aplicação

Você pode iniciar a aplicação utilizando o Maven Wrapper que já vem no projeto:

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

No Windows:

```DOS
mvnw.cmd spring-boot:run
```

A API estará rodando em http://localhost:8080.

## 📚 Documentação da API (Swagger)

A API está totalmente documentada utilizando o Swagger UI. Com a aplicação em execução, você pode acessar a interface interativa, testar os endpoints e ver os schemas (DTOs) através do navegador:

👉 Acessar Swagger UI (http://localhost:8080/swagger-ui/index.html)
Endpoints Principais (/v1/consents)

    GET /: Retorna a lista paginada de consentimentos filtrado por status.

    POST /: Cria um novo consentimento (Requer um Body com o CPF).

    GET /{id}: Busca os detalhes de um consentimento específico pelo ID.

    PATCH /{id}: Reativa/Atualiza um consentimento existente.

    DELETE /{id}: Revoga um consentimento existente.
