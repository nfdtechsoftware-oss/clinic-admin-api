package com.nfdtech.clinic_admin_api.helpers;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;

public class EspecialidadeTestBuilder {

    private String nome = "Psicologia";
    private String descricao = "Psicologia Clínica";
    private Boolean ativo = true;

    public static EspecialidadeTestBuilder umaPsicologia() {
        return new EspecialidadeTestBuilder()
                .comNome("Psicologia")
                .comDescricao("Psicologia Clínica");
    }

    public static EspecialidadeTestBuilder umaFonoaudiologia() {
        return new EspecialidadeTestBuilder()
                .comNome("Fonoaudiologia")
                .comDescricao("Fonoaudiologia Clínica");
    }

    public static EspecialidadeTestBuilder umaFisioterapia() {
        return new EspecialidadeTestBuilder()
                .comNome("Fisioterapia")
                .comDescricao("Fisioterapia Clínica");
    }

    public EspecialidadeTestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public EspecialidadeTestBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public EspecialidadeTestBuilder inativa() {
        this.ativo = false;
        return this;
    }

    public Especialidade build() {
        return Especialidade.builder()
                .nome(nome)
                .descricao(descricao)
                .ativo(ativo)
                .build();
    }
}
