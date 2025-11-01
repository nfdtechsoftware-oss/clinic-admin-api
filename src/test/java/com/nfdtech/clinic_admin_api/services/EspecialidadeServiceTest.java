package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.dto.especialidade.EspecialidadeRequestDTO;
import com.nfdtech.clinic_admin_api.dto.especialidade.EspecialidadeResponseDTO;
import com.nfdtech.clinic_admin_api.dto.especialidade.EspecialidadeUpdateDTO;
import com.nfdtech.clinic_admin_api.exception.custom.BusinessException;
import com.nfdtech.clinic_admin_api.exception.custom.ResourceNotFoundException;
import com.nfdtech.clinic_admin_api.mapper.EspecialidadeMapper;
import com.nfdtech.clinic_admin_api.repositories.EspecialidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EspecialidadeServiceTest {

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @Mock
    private EspecialidadeMapper especialidadeMapper;

    @InjectMocks
    private EspecialidadeService especialidadeService;

    private Especialidade especialidade;
    private EspecialidadeRequestDTO especialidadeRequestDTO;
    private EspecialidadeResponseDTO especialidadeResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        especialidade = Especialidade.builder()
                .id(1L)
                .nome("Psicologia")
                .descricao("Psicologia Clínica")
                .ativo(true)
                .build();

        especialidadeRequestDTO = new EspecialidadeRequestDTO();
        especialidadeRequestDTO.setNome("Psicologia");
        especialidadeRequestDTO.setDescricao("Psicologia Clínica");

        especialidadeResponseDTO = new EspecialidadeResponseDTO();
        especialidadeResponseDTO.setId(1L);
        especialidadeResponseDTO.setNome("Psicologia");
        especialidadeResponseDTO.setDescricao("Psicologia Clínica");
        especialidadeResponseDTO.setAtivo(true);
    }

    @Test
    void deveListarApenasEspecialidadesAtivas() {
        when(especialidadeRepository.findByAtivoTrue()).thenReturn(List.of(especialidade));
        when(especialidadeMapper.toResponseDTOList(anyList())).thenReturn(List.of(especialidadeResponseDTO));

        List<EspecialidadeResponseDTO> especialidades = especialidadeService.listarTodas();

        assertThat(especialidades).hasSize(1);
        assertThat(especialidades.get(0).getAtivo()).isTrue();
        assertThat(especialidades.get(0).getNome()).isEqualTo("Psicologia");
        verify(especialidadeRepository, times(1)).findByAtivoTrue();
        verify(especialidadeMapper, times(1)).toResponseDTOList(anyList());
    }

    @Test
    void deveBuscarEspecialidadePorId() {
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));
        when(especialidadeMapper.toResponseDTO(especialidade)).thenReturn(especialidadeResponseDTO);

        EspecialidadeResponseDTO encontrada = especialidadeService.buscarPorId(1L);

        assertThat(encontrada.getNome()).isEqualTo("Psicologia");
        assertThat(encontrada.getDescricao()).isEqualTo("Psicologia Clínica");
        verify(especialidadeRepository, times(1)).findById(1L);
        verify(especialidadeMapper, times(1)).toResponseDTO(especialidade);
    }

    @Test
    void deveLancarExcecaoAoBuscarEspecialidadeInexistente() {
        when(especialidadeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> especialidadeService.buscarPorId(99L));
        verify(especialidadeRepository, times(1)).findById(99L);
    }

    @Test
    void deveCriarEspecialidade() {
        when(especialidadeRepository.existsByNome("Psicologia")).thenReturn(false);
        when(especialidadeMapper.toEntity(especialidadeRequestDTO)).thenReturn(especialidade);
        when(especialidadeRepository.save(any(Especialidade.class))).thenReturn(especialidade);
        when(especialidadeMapper.toResponseDTO(especialidade)).thenReturn(especialidadeResponseDTO);

        EspecialidadeResponseDTO criada = especialidadeService.criar(especialidadeRequestDTO);

        assertThat(criada.getNome()).isEqualTo("Psicologia");
        assertThat(criada.getDescricao()).isEqualTo("Psicologia Clínica");
        verify(especialidadeRepository, times(1)).existsByNome("Psicologia");
        verify(especialidadeMapper, times(1)).toEntity(especialidadeRequestDTO);
        verify(especialidadeRepository, times(1)).save(any(Especialidade.class));
        verify(especialidadeMapper, times(1)).toResponseDTO(especialidade);
    }

    @Test
    void deveLancarExcecaoAoCriarEspecialidadeComNomeDuplicado() {
        when(especialidadeRepository.existsByNome("Psicologia")).thenReturn(true);

        assertThrows(BusinessException.class, () -> especialidadeService.criar(especialidadeRequestDTO));
        verify(especialidadeRepository, times(1)).existsByNome("Psicologia");
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

    @Test
    void deveAtualizarEspecialidade() {
        EspecialidadeUpdateDTO updateDTO = new EspecialidadeUpdateDTO();
        updateDTO.setDescricao("Nova descrição");

        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));
        when(especialidadeRepository.save(any(Especialidade.class))).thenReturn(especialidade);
        when(especialidadeMapper.toResponseDTO(especialidade)).thenReturn(especialidadeResponseDTO);

        EspecialidadeResponseDTO atualizada = especialidadeService.atualizar(1L, updateDTO);

        assertThat(atualizada).isNotNull();
        verify(especialidadeRepository, times(1)).findById(1L);
        verify(especialidadeMapper, times(1)).updateEntityFromDTO(eq(updateDTO), eq(especialidade));
        verify(especialidadeRepository, times(1)).save(especialidade);
    }

    @Test
    void deveLancarExcecaoAoAtualizarComNomeDuplicado() {
        EspecialidadeUpdateDTO updateDTO = new EspecialidadeUpdateDTO();
        updateDTO.setNome("Fonoaudiologia");

        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));
        when(especialidadeRepository.existsByNome("Fonoaudiologia")).thenReturn(true);

        assertThrows(BusinessException.class, () -> especialidadeService.atualizar(1L, updateDTO));
        verify(especialidadeRepository, times(1)).existsByNome("Fonoaudiologia");
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

    @Test
    void deveDesativarEspecialidade() {
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));

        especialidadeService.desativar(1L);

        assertThat(especialidade.getAtivo()).isFalse();
        verify(especialidadeRepository, times(1)).findById(1L);
        verify(especialidadeRepository, times(1)).save(especialidade);
    }

    @Test
    void deveDeletarEspecialidade() {
        when(especialidadeRepository.existsById(1L)).thenReturn(true);

        especialidadeService.deletar(1L);

        verify(especialidadeRepository, times(1)).existsById(1L);
        verify(especialidadeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarEspecialidadeInexistente() {
        when(especialidadeRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> especialidadeService.deletar(99L));
        verify(especialidadeRepository, times(1)).existsById(99L);
        verify(especialidadeRepository, never()).deleteById(any());
    }

    @Test
    void deveLancarExcecaoAoDesativarEspecialidadeInexistente() {
        when(especialidadeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> especialidadeService.desativar(99L));
        verify(especialidadeRepository, times(1)).findById(99L);
        verify(especialidadeRepository, never()).save(any(Especialidade.class));
    }

}
