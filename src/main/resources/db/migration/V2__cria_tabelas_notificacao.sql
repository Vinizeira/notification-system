-- V2__cria_tabelas_notificacao.sql

CREATE TABLE guias (
                       id UUID PRIMARY KEY,
                       tipo_guia VARCHAR(100) NOT NULL,
                       nome_empresa_normalizado VARCHAR(255) NOT NULL,
                       mes INTEGER NOT NULL,
                       ano INTEGER NOT NULL,
                       vencimento DATE NOT NULL,
                       cliente_id UUID NULL REFERENCES clientes(id),
                       status VARCHAR(50) NOT NULL,
                       criado_em TIMESTAMP WITH TIME ZONE NOT NULL,
                       CONSTRAINT uk_guia_natural UNIQUE (tipo_guia, nome_empresa_normalizado, mes, ano)
);

CREATE TABLE historico_notificacao (
                                       id UUID PRIMARY KEY,
                                       guia_id UUID NOT NULL REFERENCES guias(id),
                                       canal VARCHAR(20) NOT NULL,
                                       resultado VARCHAR(20) NOT NULL,
                                       data_hora TIMESTAMP WITH TIME ZONE NOT NULL,
                                       motivo_falha VARCHAR(500) NULL
);

CREATE TABLE pendencia_cadastro_cliente (
                                            id UUID PRIMARY KEY,
                                            tipo_guia VARCHAR(100) NOT NULL,
                                            nome_empresa VARCHAR(255) NOT NULL,
                                            mes INTEGER NOT NULL,
                                            ano INTEGER NOT NULL,
                                            criado_em TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE avisos_contadora (
                                  id UUID PRIMARY KEY,
                                  mensagem VARCHAR(1000) NOT NULL,
                                  criado_em TIMESTAMP WITH TIME ZONE NOT NULL,
                                  guia_id UUID NULL REFERENCES guias(id)
);
