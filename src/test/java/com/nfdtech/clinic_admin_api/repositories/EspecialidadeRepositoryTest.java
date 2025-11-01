package com.nfdtech.clinic_admin_api.repositories;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.helpers.EspecialidadeTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EspecialidadeRepositoryTest {

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Test
    void deveSalvarEBuscarEspecialidadePorNome() {
        Especialidade especialidade = EspecialidadeTestBuilder.umaPsicologia()
                .comNome("Psicologia Test Save")
                .build();

        especialidadeRepository.save(especialidade);
        Optional<Especialidade> encontrada = especialidadeRepository.findByNome("Psicologia Test Save");

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo("Psicologia Test Save");
        assertThat(encontrada.get().getDescricao()).isEqualTo("Psicologia Clínica");
    }

    @Test
    void deveVerificarSeEspecialidadeExiste() {
        Especialidade especialidade = EspecialidadeTestBuilder.umaFonoaudiologia()
                .comNome("Fonoaudiologia Test Exists")
                .build();

        especialidadeRepository.save(especialidade);

        boolean existe = especialidadeRepository.existsByNome("Fonoaudiologia Test Exists");
        boolean naoExiste = especialidadeRepository.existsByNome("Fisioterapia Inexistente");

        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    void deveListarApenasEspecialidadesAtivas() {
        Especialidade ativa = EspecialidadeTestBuilder.umaPsicologia()
                .comNome("Psicologia Test Ativa")
                .build();

        Especialidade inativa = EspecialidadeTestBuilder.umaFonoaudiologia()
                .comNome("Fonoaudiologia Test Inativa")
                .inativa()
                .build();

        especialidadeRepository.save(ativa);
        especialidadeRepository.save(inativa);

        List<Especialidade> ativas = especialidadeRepository.findByAtivoTrue();

        assertThat(ativas).hasSizeGreaterThanOrEqualTo(1);
        assertThat(ativas).anyMatch(e -> e.getNome().equals("Psicologia Test Ativa") && e.getAtivo());
    }

    @Test
    void deveBuscarEspecialidadePorNomeExato() {
        Especialidade psicologia = EspecialidadeTestBuilder.umaPsicologia()
                .comNome("Psicologia Test Find")
                .build();

        especialidadeRepository.save(psicologia);

        Optional<Especialidade> encontrada = especialidadeRepository.findByNome("Psicologia Test Find");

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo("Psicologia Test Find");
        assertThat(encontrada.get().getDescricao()).isEqualTo("Psicologia Clínica");
    }

    @Test
    void deveRetornarEmptyQuandoNaoEncontrarEspecialidadePorNome() {
        Optional<Especialidade> encontrada = especialidadeRepository.findByNome("Nome Totalmente Inexistente");

        assertThat(encontrada).isEmpty();
    }

    @Test
    void deveDeletarEspecialidade() {
        Especialidade especialidade = EspecialidadeTestBuilder.umaPsicologia()
                .comNome("Psicologia Test Delete")
                .build();

        Especialidade salva = especialidadeRepository.save(especialidade);
        Long id = salva.getId();

        assertThat(especialidadeRepository.existsById(id)).isTrue();

        especialidadeRepository.deleteById(id);

        assertThat(especialidadeRepository.existsById(id)).isFalse();
    }

    @Test
    void deveAtualizarEspecialidade() {
        Especialidade especialidade = EspecialidadeTestBuilder.umaPsicologia()
                .comNome("Psicologia Test Update")
                .build();

        Especialidade salva = especialidadeRepository.save(especialidade);

        salva.setDescricao("Nova descrição atualizada");
        salva.setAtivo(false);

        Especialidade atualizada = especialidadeRepository.save(salva);

        assertThat(atualizada.getDescricao()).isEqualTo("Nova descrição atualizada");
        assertThat(atualizada.getAtivo()).isFalse();
    }

}
