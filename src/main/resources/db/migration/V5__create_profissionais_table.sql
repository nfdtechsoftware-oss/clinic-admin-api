-- Criar tabela profissionais
CREATE TABLE profissionais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE,
    registro_classe VARCHAR(50) NOT NULL UNIQUE,

    -- Endereço
    logradouro VARCHAR(200),
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(9),

    -- Contato
    telefone VARCHAR(20),
    celular VARCHAR(20),
    email VARCHAR(100),

    -- Controle
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    -- Auditoria
    created_by VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

-- Criar tabela de relacionamento profissionais_especialidades (ManyToMany)
CREATE TABLE profissionais_especialidades (
    profissional_id BIGINT NOT NULL,
    especialidade_id BIGINT NOT NULL,
    PRIMARY KEY (profissional_id, especialidade_id),
    CONSTRAINT fk_prof_espec_profissional FOREIGN KEY (profissional_id)
        REFERENCES profissionais(id) ON DELETE CASCADE,
    CONSTRAINT fk_prof_espec_especialidade FOREIGN KEY (especialidade_id)
        REFERENCES especialidades(id) ON DELETE CASCADE
);

-- Criar índices para melhorar performance
CREATE INDEX idx_profissionais_cpf ON profissionais(cpf);
CREATE INDEX idx_profissionais_registro_classe ON profissionais(registro_classe);
CREATE INDEX idx_profissionais_nome ON profissionais(nome);
CREATE INDEX idx_profissionais_ativo ON profissionais(ativo);
CREATE INDEX idx_prof_espec_profissional ON profissionais_especialidades(profissional_id);
CREATE INDEX idx_prof_espec_especialidade ON profissionais_especialidades(especialidade_id);

-- Comentários
COMMENT ON TABLE profissionais IS 'Tabela de profissionais/terapeutas da clínica';
COMMENT ON COLUMN profissionais.id IS 'Identificador único do profissional';
COMMENT ON COLUMN profissionais.registro_classe IS 'Registro profissional (CRP, CRFa, CREFITO, etc)';
COMMENT ON TABLE profissionais_especialidades IS 'Relacionamento ManyToMany entre profissionais e especialidades';
