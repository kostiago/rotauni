CREATE TABLE usuario (
    id                BIGSERIAL    PRIMARY KEY,
    municipio_id      BIGINT       REFERENCES municipio (id),
    nome              VARCHAR(150) NOT NULL,
    cpf               VARCHAR(11)  NOT NULL CHECK (cpf ~ '^[0-9]{11}$'),
    email             VARCHAR(254) NOT NULL,
    telefone          VARCHAR(20),
    senha_hash        VARCHAR(100) NOT NULL,
    papel             VARCHAR(20)  NOT NULL
                      CHECK (papel IN ('SUPERADMIN', 'ADMIN', 'EDITOR', 'LEITOR', 'ESTUDANTE', 'MOTORISTA')),
    ativo             BOOLEAN      NOT NULL DEFAULT TRUE,
    ultimo_acesso_em  TIMESTAMPTZ,
    criado_em         TIMESTAMPTZ  NOT NULL DEFAULT now(),
    atualizado_em     TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT usuario_municipio_conforme_papel
        CHECK ((papel = 'SUPERADMIN') = (municipio_id IS NULL))
);

-- E-mail é o login: único na plataforma, sem diferenciar maiúsculas de minúsculas.
CREATE UNIQUE INDEX usuario_email_unico ON usuario (lower(email));

-- CPF é único dentro de cada município.
CREATE UNIQUE INDEX usuario_cpf_por_municipio ON usuario (municipio_id, cpf)
    WHERE municipio_id IS NOT NULL;

CREATE INDEX usuario_municipio ON usuario (municipio_id);