CREATE TABLE municipio (
    id           BIGSERIAL    PRIMARY KEY,
    nome         VARCHAR(120) NOT NULL,
    uf           VARCHAR(2)   NOT NULL CHECK (uf ~ '^[A-Z]{2}$'),
    codigo_ibge  VARCHAR(7)   NOT NULL UNIQUE CHECK (codigo_ibge ~ '^[0-9]{7}$'),
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em    TIMESTAMPTZ  NOT NULL DEFAULT now()
);