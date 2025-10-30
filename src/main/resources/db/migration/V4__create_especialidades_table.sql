-- Criar tabela especialidades
CREATE TABLE especialidades (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    created_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

-- Criar índice para melhorar performance de busca por nome
CREATE INDEX idx_especialidades_nome ON especialidades(nome);

-- Criar índice para melhorar performance de busca por ativo
CREATE INDEX idx_especialidades_ativo ON especialidades(ativo);

-- Inserir especialidades iniciais (voltadas para terapias e saúde integral)
INSERT INTO especialidades (nome, descricao, ativo, created_by, created_at) VALUES
('Fonoaudiologia', 'Tratamento de distúrbios da comunicação, linguagem, fala, voz e audição', true, 'system', CURRENT_TIMESTAMP),
('Psicologia', 'Avaliação e tratamento de questões emocionais, comportamentais e cognitivas', true, 'system', CURRENT_TIMESTAMP),
('Musicoterapia', 'Uso da música como ferramenta terapêutica para desenvolvimento e reabilitação', true, 'system', CURRENT_TIMESTAMP),
('Terapia Ocupacional', 'Tratamento focado em desenvolver habilidades para atividades da vida diária', true, 'system', CURRENT_TIMESTAMP),
('Fisioterapia', 'Tratamento de disfunções físicas através de exercícios e técnicas manuais', true, 'system', CURRENT_TIMESTAMP),
('Psicopedagogia', 'Tratamento de dificuldades de aprendizagem e desenvolvimento cognitivo', true, 'system', CURRENT_TIMESTAMP),
('Nutrição', 'Orientação alimentar e nutricional para saúde e tratamento de patologias', true, 'system', CURRENT_TIMESTAMP),
('Pedagogia', 'Orientação pedagógica e educacional especializada', true, 'system', CURRENT_TIMESTAMP),
('Neuropsicologia', 'Avaliação e reabilitação de funções cognitivas e comportamentais', true, 'system', CURRENT_TIMESTAMP),
('Psicomotricidade', 'Trabalho com desenvolvimento motor, cognitivo e emocional integrados', true, 'system', CURRENT_TIMESTAMP);

-- Comentários da tabela
COMMENT ON TABLE especialidades IS 'Tabela de especialidades médicas';
COMMENT ON COLUMN especialidades.id IS 'Identificador único da especialidade';
COMMENT ON COLUMN especialidades.nome IS 'Nome da especialidade médica';
COMMENT ON COLUMN especialidades.descricao IS 'Descrição detalhada da especialidade';
COMMENT ON COLUMN especialidades.ativo IS 'Indica se a especialidade está ativa';
COMMENT ON COLUMN especialidades.created_by IS 'Usuário que criou o registro';
COMMENT ON COLUMN especialidades.created_at IS 'Data e hora de criação do registro';
COMMENT ON COLUMN especialidades.updated_by IS 'Usuário que atualizou o registro pela última vez';
COMMENT ON COLUMN especialidades.updated_at IS 'Data e hora da última atualização do registro';
