-- Adicionar campos de auditoria à tabela users
-- Esta migration adiciona rastreamento de quem criou e modificou registros

-- Adicionar coluna created_by
ALTER TABLE users ADD COLUMN created_by VARCHAR(50);

-- Adicionar coluna updated_by
ALTER TABLE users ADD COLUMN updated_by VARCHAR(50);

-- Atualizar registros existentes com valor padrão
UPDATE users SET created_by = 'system' WHERE created_by IS NULL;
UPDATE users SET updated_by = 'system' WHERE updated_by IS NULL;

-- Tornar as colunas NOT NULL após popular
ALTER TABLE users ALTER COLUMN created_by SET NOT NULL;

-- updated_by pode ser NULL (será preenchido na primeira atualização)

-- IMPORTANTE: Remover constraint ANTES de atualizar os valores
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_role_check;

-- Corrigir valores de role para o novo padrão
UPDATE users SET role = 'PROFESSIONAL' WHERE role = 'PROFISSIONAL';
UPDATE users SET role = 'RECEPTION' WHERE role = 'RECEPCAO';

-- Adicionar novo constraint com valores corretos
ALTER TABLE users ADD CONSTRAINT users_role_check CHECK (role IN ('ADMIN', 'RECEPTION', 'PROFESSIONAL'));

-- Comentários nas colunas para documentação
COMMENT ON COLUMN users.created_by IS 'Username de quem criou o registro';
COMMENT ON COLUMN users.updated_by IS 'Username de quem atualizou o registro pela última vez';
COMMENT ON COLUMN users.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN users.updated_at IS 'Data e hora da última atualização do registro';
