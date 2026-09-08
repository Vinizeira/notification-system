CREATE TABLE clientes (
                          id UUID PRIMARY KEY,
                          nome_empresa VARCHAR(255) NOT NULL,
                          nome_empresa_normalizado VARCHAR(255) NOT NULL UNIQUE,
                          email VARCHAR(255) NOT NULL,
                          telefone_whatsapp VARCHAR(30) NOT NULL,
                          criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);