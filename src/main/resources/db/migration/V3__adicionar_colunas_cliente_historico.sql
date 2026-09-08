ALTER TABLE historico_notificacao ADD COLUMN IF NOT EXISTS nome_empresa VARCHAR(255);
ALTER TABLE historico_notificacao ADD COLUMN IF NOT EXISTS email VARCHAR(255);
ALTER TABLE historico_notificacao ADD COLUMN IF NOT EXISTS telefone_whatsapp VARCHAR(255);