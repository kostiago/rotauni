-- Executado pelo PostgreSQL só na primeira inicialização do container
-- (quando o volume de dados está vazio).
-- O banco principal (rotauni) já é criado pela variável POSTGRES_DB.
CREATE DATABASE rotauni_test OWNER rotauni;