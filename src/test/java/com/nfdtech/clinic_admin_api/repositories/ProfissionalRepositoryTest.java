package com.nfdtech.clinic_admin_api.repositories;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.domain.profissional.Profissional;
import com.nfdtech.clinic_admin_api.helpers.EspecialidadeTestBuilder;
import com.nfdtech.clinic_admin_api.helpers.ProfissionalTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProfissionalRepositoryTest {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Especialidade criarEspecialidade(String nome, String descricao) {
        return especialidadeRepository.save(Especialidade.builder()
                .nome(nome)
                .descricao(descricao)
                .ativo(true)
                .build());
    }

    @Test
    void deveSalvarEBuscarProfissionalPorRegistroClasse() {
        Especialidade especialidade = criarEspecialidade("Psicologia Test 1", "Psicologia Clínica");

        Profissional profissional = ProfissionalTestBuilder.umPsicologo()
                .comNome("Maria Silva Santos")
                .comRegistroClasse("CRP 01/12345")
                .comEspecialidade(especialidade)
                .build();

        profissionalRepository.save(profissional);
        Optional<Profissional> encontrado = profissionalRepository.findByRegistroClasse("CRP 01/12345");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Maria Silva Santos");
        assertThat(encontrado.get().getRegistroClasse()).isEqualTo("CRP 01/12345");
    }

    @Test
    void deveVerificarSeRegistroClasseExiste() {
        Especialidade especialidade = criarEspecialidade("Fonoaudiologia Test 2", "Fonoaudiologia Clínica");

        Profissional profissional = ProfissionalTestBuilder.umFisioterapeuta()
                .comNome("João Silva")
                .comRegistroClasse("CREFITO 02/98765")
                .comEspecialidade(especialidade)
                .build();

        profissionalRepository.save(profissional);

        boolean existe = profissionalRepository.existsByRegistroClasse("CREFITO 02/98765");
        boolean naoExiste = profissionalRepository.existsByRegistroClasse("CRP 99/99999");

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    void deveListarApenasProfissionaisAtivos() {
        Especialidade especialidade = criarEspecialidade("Psicologia Test 3", "Psicologia Clínica");

        Profissional ativo = ProfissionalTestBuilder.umProfissional()
                .comNome("Maria Ativa")
                .comRegistroClasse("CRP 01/11111")
                .comEspecialidade(especialidade)
                .build();

        Profissional inativo = ProfissionalTestBuilder.umProfissional()
                .comNome("João Inativo")
                .comRegistroClasse("CRP 02/22222")
                .comEspecialidade(especialidade)
                .inativo()
                .build();

        profissionalRepository.save(ativo);
        profissionalRepository.save(inativo);

        List<Profissional> ativos = profissionalRepository.findByAtivoTrue();

        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getNome()).isEqualTo("Maria Ativa");
        assertThat(ativos.get(0).getAtivo()).isTrue();
    }

    @Test
    void deveBuscarProfissionaisPorEspecialidade() {
        Especialidade psicologia = criarEspecialidade("Psicologia Test 4", "Psicologia Clínica");
        Especialidade fonoaudiologia = criarEspecialidade("Fonoaudiologia Test 4", "Fonoaudiologia Clínica");

        Profissional psicologo = ProfissionalTestBuilder.umPsicologo()
                .comNome("Psicólogo 1")
                .comRegistroClasse("CRP 03/33333")
                .comEspecialidade(psicologia)
                .build();

        Profissional fonoaudiologo = ProfissionalTestBuilder.umFonoaudiologo()
                .comNome("Fonoaudiólogo 1")
                .comRegistroClasse("CRFa 04/44444")
                .comEspecialidade(fonoaudiologia)
                .build();

        profissionalRepository.save(psicologo);
        profissionalRepository.save(fonoaudiologo);

        List<Profissional> psicologos = profissionalRepository.findByEspecialidadeId(psicologia.getId());
        List<Profissional> fonos = profissionalRepository.findByEspecialidadeId(fonoaudiologia.getId());

        assertThat(psicologos).hasSize(1);
        assertThat(psicologos.get(0).getNome()).isEqualTo("Psicólogo 1");

        assertThat(fonos).hasSize(1);
        assertThat(fonos.get(0).getNome()).isEqualTo("Fonoaudiólogo 1");
    }

    @Test
    void deveBuscarProfissionaisPorNomeIgnorandoCaseEApenaosAtivos() {
        Especialidade especialidade = criarEspecialidade("Psicologia Test 5", "Psicologia Clínica");

        Profissional maria = ProfissionalTestBuilder.umProfissional()
                .comNome("Maria Silva Santos")
                .comRegistroClasse("CRP 05/55555")
                .comEspecialidade(especialidade)
                .build();

        Profissional mariana = ProfissionalTestBuilder.umProfissional()
                .comNome("Mariana Costa")
                .comRegistroClasse("CRP 06/66666")
                .comEspecialidade(especialidade)
                .inativo()
                .build();

        profissionalRepository.save(maria);
        profissionalRepository.save(mariana);

        List<Profissional> encontrados = profissionalRepository.findByNomeContainingIgnoreCaseAndAtivoTrue("maria");

        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getNome()).isEqualTo("Maria Silva Santos");
        assertThat(encontrados.get(0).getAtivo()).isTrue();
    }

    @Test
    void deveBuscarProfissionalComMultiplasEspecialidades() {
        Especialidade psicologia = criarEspecialidade("Psicologia Test 6", "Psicologia Clínica");
        Especialidade fonoaudiologia = criarEspecialidade("Fonoaudiologia Test 6", "Fonoaudiologia Clínica");

        Profissional multiplo = ProfissionalTestBuilder.umProfissional()
                .comNome("Profissional Multiplo")
                .comRegistroClasse("CRP 07/77777")
                .comEspecialidade(psicologia)
                .comEspecialidade(fonoaudiologia)
                .build();

        profissionalRepository.save(multiplo);
        entityManager.flush();
        entityManager.clear();

        Optional<Profissional> encontrado = profissionalRepository.findById(multiplo.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEspecialidades()).hasSize(2);
        assertThat(encontrado.get().getEspecialidades())
                .extracting(Especialidade::getNome)
                .containsExactlyInAnyOrder("Psicologia Test 6", "Fonoaudiologia Test 6");
    }

}
