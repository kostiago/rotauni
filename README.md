# RotaUni

Plataforma para prefeituras gerirem o transporte de estudantes universitários que viajam diariamente para outras cidades.

- **Painel web**: o setor de transporte da prefeitura analisa inscrições, monta rotas e acompanha a ocupação dos ônibus.
- **App mobile**: o estudante se inscreve e usa a carteirinha digital; o motorista registra os embarques, mesmo sem internet.

> **Status:** em desenvolvimento. Etapa atual: fundação do backend.

## Stack

| Parte | Tecnologia |
|---|---|
| API | Java 25, Spring Boot 4.1, Spring Data JPA, Flyway |
| Banco de dados | PostgreSQL 16 + PostGIS 3.4 |
| Painel web | HTML, CSS e JavaScript (Vite) |
| App | React Native com Expo |
| Ambiente local | Docker Compose |

## Estrutura

```
rotauni/
├── api/                 # Backend Spring Boot (API REST)
├── painel/              # Painel web da prefeitura
├── app/                 # App mobile (estudante e motorista)
└── docker-compose.yml   # Serviços de desenvolvimento (banco de dados)
```

## Como rodar localmente

### Pré-requisitos

- JDK 25 (com a variável `JAVA_HOME` configurada)
- Docker Desktop
- Git

O Maven não precisa ser instalado: o projeto usa o Maven Wrapper (`mvnw`).

### 1. Subir o banco de dados

Na raiz do projeto:

```bash
docker compose up -d
```

O PostgreSQL fica disponível em `localhost:5432` (banco, usuário e senha: `rotauni`).

### 2. Rodar a API

```bash
cd api
./mvnw spring-boot:run      # Linux/macOS
.\mvnw spring-boot:run      # Windows
```

Na inicialização, o Flyway aplica as migrações do banco automaticamente. A API sobe em `http://localhost:8080`.

### Variáveis de ambiente

Em desenvolvimento, os valores padrão já funcionam. Em produção, configure:

| Variável | Descrição | Padrão (dev) |
|---|---|---|
| `ROTAUNI_DB_URL` | URL JDBC do banco | `jdbc:postgresql://localhost:5432/rotauni` |
| `ROTAUNI_DB_USUARIO` | Usuário do banco | `rotauni` |
| `ROTAUNI_DB_SENHA` | Senha do banco | `rotauni` |

## Banco de dados

O esquema é versionado com **Flyway**, em `api/src/main/resources/db/migration`.

- Cada mudança no banco é um novo arquivo `V<número>__<descricao>.sql`.
- **Uma migração já aplicada nunca é editada.** Para corrigir algo, crie uma nova migração.
- O Hibernate apenas valida as entidades contra o banco (`ddl-auto: validate`); ele não cria nem altera tabelas.

## Padrão de commits

Baseado em [padroes-de-commits](https://github.com/iuricode/padroes-de-commits), acrescido de escopo:

```
:emoji: tipo(escopo): Descrição curta
```

- **Escopo:** `api`, `painel`, `app` ou `infra` (opcional quando a mudança é geral).
- **Descrição:** em português, iniciando com maiúscula, sem ponto final.
- **Um commit, um assunto.**

| Situação | Exemplo |
|---|---|
| Nova funcionalidade | `:sparkles: feat(api): Cadastro de municípios` |
| Correção de bug | `:bug: fix(api): Validação do CPF` |
| Migração / dados | `:card_file_box: raw(api): Tabela de municípios` |
| Testes | `:test_tube: test(api): Testes do login` |
| Nova dependência | `:heavy_plus_sign: build(api): Spring Security` |
| Configuração | `:wrench: chore(infra): Docker Compose` |
| Documentação | `:books: docs: README do projeto` |
| Refatoração | `:recycle: refactor(api): Serviço de usuários` |

## Licença

Software proprietário. Todos os direitos reservados.
