package com.nfdtech.clinic_admin_api.helpers;

import com.nfdtech.clinic_admin_api.domain.embeddable.Contato;
import com.nfdtech.clinic_admin_api.domain.embeddable.Endereco;
import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.domain.profissional.Profissional;
import com.nfdtech.clinic_admin_api.domain.valueobject.CPF;
import com.nfdtech.clinic_admin_api.domain.valueobject.Telefone;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ProfissionalTestBuilder {

    // CPFs válidos para testes
    private static final String[] CPFS_VALIDOS = {
            "762.272.230-85",
            "259.393.010-10",
            "023.693.460-00",
            "395.974.480-33",
            "444.726.660-14",
            "777.278.510-00",
            "256.469.380-66",
            "839.635.760-90",
            "308.516.270-48",
            "123.456.789-09"
    };

    private static int cpfIndex = 0;

    private String nome = "Maria Silva Santos";
    private String cpf = getProximoCPF();
    private LocalDate dataNascimento = LocalDate.of(1985, 5, 15);
    private String registroClasse = "CRP 01/12345";
    private String logradouro = "Rua das Flores";
    private String numero = "123";
    private String complemento = null;
    private String bairro = "Centro";
    private String cidade = "São Paulo";
    private String estado = "SP";
    private String cep = "01234-567";
    private String telefone = null;
    private String celular = "11987654321";
    private String email = "teste@clinica.com";
    private Set<Especialidade> especialidades = new HashSet<>();
    private Boolean ativo = true;

    public static ProfissionalTestBuilder umProfissional() {
        return new ProfissionalTestBuilder();
    }

    public static ProfissionalTestBuilder umPsicologo() {
        return new ProfissionalTestBuilder()
                .comNome("Dr. João Psicólogo")
                .comRegistroClasse("CRP 01/12345");
    }

    public static ProfissionalTestBuilder umFonoaudiologo() {
        return new ProfissionalTestBuilder()
                .comNome("Dra. Ana Fonoaudióloga")
                .comRegistroClasse("CRFa 02/98765");
    }

    public static ProfissionalTestBuilder umFisioterapeuta() {
        return new ProfissionalTestBuilder()
                .comNome("Dr. Carlos Fisioterapeuta")
                .comRegistroClasse("CREFITO 03/54321");
    }

    private static synchronized String getProximoCPF() {
        String cpf = CPFS_VALIDOS[cpfIndex];
        cpfIndex = (cpfIndex + 1) % CPFS_VALIDOS.length;
        return cpf;
    }

    public ProfissionalTestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public ProfissionalTestBuilder comCPF(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public ProfissionalTestBuilder comDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public ProfissionalTestBuilder comRegistroClasse(String registroClasse) {
        this.registroClasse = registroClasse;
        return this;
    }

    public ProfissionalTestBuilder comEndereco(String logradouro, String numero, String bairro, String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        return this;
    }

    public ProfissionalTestBuilder comComplemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public ProfissionalTestBuilder comTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public ProfissionalTestBuilder comCelular(String celular) {
        this.celular = celular;
        return this;
    }

    public ProfissionalTestBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public ProfissionalTestBuilder comEspecialidade(Especialidade especialidade) {
        this.especialidades.add(especialidade);
        return this;
    }

    public ProfissionalTestBuilder comEspecialidades(Set<Especialidade> especialidades) {
        this.especialidades = new HashSet<>(especialidades);
        return this;
    }

    public ProfissionalTestBuilder inativo() {
        this.ativo = false;
        return this;
    }

    public Profissional build() {
        Endereco endereco = Endereco.builder()
                .logradouro(logradouro)
                .numero(numero)
                .complemento(complemento)
                .bairro(bairro)
                .cidade(cidade)
                .estado(estado)
                .cep(cep)
                .build();

        Contato contato = Contato.builder()
                .telefone(telefone != null ? Telefone.of(telefone) : null)
                .celular(celular != null ? Telefone.of(celular) : null)
                .email(email)
                .build();

        return Profissional.builder()
                .nome(nome)
                .cpf(CPF.of(cpf))
                .dataNascimento(dataNascimento)
                .registroClasse(registroClasse)
                .endereco(endereco)
                .contato(contato)
                .especialidades(especialidades)
                .ativo(ativo)
                .build();
    }
}
