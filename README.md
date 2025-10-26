# Clinic Admin API

Sistema de Gestão de Clínicas de Especialidades desenvolvido com Java 21, Spring Boot e PostgreSQL.

## 🚀 Tecnologias

- **Java 21** - LTS com features modernas
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security + JWT** - Autenticação e autorização
- **PostgreSQL 16** - Banco de dados
- **Flyway** - Versionamento de schema
- **Lombok** - Redução de boilerplate
- **MapStruct** - Mapeamento de objetos
- **SpringDoc OpenAPI** - Documentação da API

## 📋 Pré-requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL 16+
- Docker (opcional, para rodar o banco via container)

## ⚙️ Configuração

### 1. Variáveis de Ambiente

Copie o arquivo `.env.example` para `.env` e ajuste as configurações:

```bash
cp .env.example .env
```

### 2. Banco de Dados

Crie o banco de dados PostgreSQL:

```sql
CREATE DATABASE clinic_db;
```

As migrations do Flyway serão executadas automaticamente na inicialização.

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8082/clinic`

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8082/clinic/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8082/clinic/api-docs

## 🔐 Módulos Implementados

### ✅ Módulo de Usuários
- CRUD completo de usuários
- Roles: ADMIN, RECEPCAO, PROFISSIONAL
- Auditoria automática (created_by, updated_by, created_at, updated_at)
- Soft delete (desativação)

### 🚧 Em Desenvolvimento
- Autenticação JWT
- Cadastro de Profissionais e Pacientes
- Agendamento de Consultas
- Módulo Financeiro

## 🏗️ Estrutura do Projeto

```
src/main/java/com/nfdtech/clinic_admin_api/
├── config/              # Configurações (Security, JPA Auditing, etc)
├── controllers/         # Endpoints REST
├── domain/              # Entidades JPA
│   ├── base/           # Entidades base (Auditavel)
│   └── user/           # Módulo de usuários
├── repositories/        # Interfaces JPA
└── services/           # Lógica de negócio

src/main/resources/
├── db/migration/        # Scripts Flyway
└── application.yml      # Configurações da aplicação
```

## 🧪 Testes

```bash
mvn test
```

## 📝 Convenções de Commit

Seguimos o padrão [Conventional Commits](https://www.conventionalcommits.org/):

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `chore`: Alterações de configuração/build
- `refactor`: Refatoração de código
- `test`: Adição ou correção de testes
- `docs`: Documentação

## 👥 Autor

**NFDTech Software**

## 📄 Licença

Este projeto é propriedade privada da NFDTech.
