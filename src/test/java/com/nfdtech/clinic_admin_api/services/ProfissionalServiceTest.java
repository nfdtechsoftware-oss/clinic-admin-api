package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.embeddable.Contato;
import com.nfdtech.clinic_admin_api.domain.embeddable.Endereco;
import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.domain.profissional.Profissional;
import com.nfdtech.clinic_admin_api.domain.valueobject.CPF;
import com.nfdtech.clinic_admin_api.domain.valueobject.Telefone;
import com.nfdtech.clinic_admin_api.dto.profissional.ProfissionalRequestDTO;
import com.nfdtech.clinic_admin_api.dto.profissional.ProfissionalResponseDTO;
import com.nfdtech.clinic_admin_api.dto.profissional.ProfissionalUpdateDTO;
import com.nfdtech.clinic_admin_api.exception.custom.BusinessException;
import com.nfdtech.clinic_admin_api.exception.custom.ResourceNotFoundException;
import com.nfdtech.clinic_admin_api.mapper.ProfissionalMapper;
import com.nfdtech.clinic_admin_api.repositories.EspecialidadeRepository;
import com.nfdtech.clinic_admin_api.repositories.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private EspecialidadeRepository especialidadeRepository;

    @Mock
    private ProfissionalMapper profissionalMapper;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional profissional;
    private ProfissionalRequestDTO profissionalRequestDTO;
    private ProfissionalResponseDTO profissionalResponseDTO;
    private Especialidade especialidade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        especialidade = Especialidade.builder()
                .id(1L)
                .nome("Psicologia")
                .descricao("Psicologia Clínica")
                .ativo(true)
                .build();

        profissional = Profissional.builder()
                .id(1L)
                .nome("Maria Silva Santos")
                .cpf(CPF.of("762.272.230-85"))
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .registroClasse("CRP 01/12345")
                .endereco(Endereco.builder()
                        .logradouro("Rua das Flores")
                        .numero("123")
                        .bairro("Centro")
                        .cidade("São Paulo")
                        .estado("SP")
                        .cep("01234-567")
                        .build())
                .contato(Contato.builder()
                        .celular(Telefone.of("11987654321"))
                        .email("maria.silva@clinica.com")
                        .build())
                .especialidades(new HashSet<>(Set.of(especialidade)))
                .ativo(true)
                .build();

        profissionalResponseDTO = ProfissionalResponseDTO.builder()
                .id(1L)
                .nome("Maria Silva Santos")
                .cpf("762.272.230-85")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .registroClasse("CRP 01/12345")
                .ativo(true)
                .build();

        profissionalRequestDTO = new ProfissionalRequestDTO();
        profissionalRequestDTO.setNome("Maria Silva Santos");
        profissionalRequestDTO.setCpf("762.272.230-85");
        profissionalRequestDTO.setDataNascimento(LocalDate.of(1985, 5, 15));
        profissionalRequestDTO.setRegistroClasse("CRP 01/12345");
        profissionalRequestDTO.setLogradouro("Rua das Flores");
        profissionalRequestDTO.setNumero("123");
        profissionalRequestDTO.setBairro("Centro");
        profissionalRequestDTO.setCidade("São Paulo");
        profissionalRequestDTO.setEstado("SP");
        profissionalRequestDTO.setCep("01234-567");
        profissionalRequestDTO.setCelular("11987654321");
        profissionalRequestDTO.setEmail("maria.silva@clinica.com");
        profissionalRequestDTO.setEspecialidadesIds(Set.of(1L));
    }

    @Test
    void deveListarApenasProfissionaisAtivos() {
        when(profissionalRepository.findByAtivoTrue()).thenReturn(List.of(profissional));
        when(profissionalMapper.toResponseDTOList(anyList())).thenReturn(List.of(profissionalResponseDTO));

        List<ProfissionalResponseDTO> profissionais = profissionalService.listarTodos();

        assertThat(profissionais).hasSize(1);
        assertThat(profissionais.get(0).getAtivo()).isTrue();
        assertThat(profissionais.get(0).getNome()).isEqualTo("Maria Silva Santos");
        verify(profissionalRepository, times(1)).findByAtivoTrue();
        verify(profissionalMapper, times(1)).toResponseDTOList(anyList());
    }

    @Test
    void deveBuscarProfissionalPorId() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalMapper.toResponseDTO(profissional)).thenReturn(profissionalResponseDTO);

        ProfissionalResponseDTO encontrado = profissionalService.buscarPorId(1L);

        assertThat(encontrado.getNome()).isEqualTo("Maria Silva Santos");
        assertThat(encontrado.getRegistroClasse()).isEqualTo("CRP 01/12345");
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalMapper, times(1)).toResponseDTO(profissional);
    }

    @Test
    void deveLancarExcecaoAoBuscarProfissionalInexistente() {
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profissionalService.buscarPorId(99L));
        verify(profissionalRepository, times(1)).findById(99L);
    }

    @Test
    void deveBuscarProfissionaisPorEspecialidade() {
        when(profissionalRepository.findByEspecialidadeId(1L)).thenReturn(List.of(profissional));
        when(profissionalMapper.toResponseDTOList(anyList())).thenReturn(List.of(profissionalResponseDTO));

        List<ProfissionalResponseDTO> profissionais = profissionalService.buscarPorEspecialidade(1L);

        assertThat(profissionais).hasSize(1);
        assertThat(profissionais.get(0).getNome()).isEqualTo("Maria Silva Santos");
        verify(profissionalRepository, times(1)).findByEspecialidadeId(1L);
        verify(profissionalMapper, times(1)).toResponseDTOList(anyList());
    }

    @Test
    void deveBuscarProfissionaisPorNome() {
        when(profissionalRepository.findByNomeContainingIgnoreCaseAndAtivoTrue("Maria"))
                .thenReturn(List.of(profissional));
        when(profissionalMapper.toResponseDTOList(anyList())).thenReturn(List.of(profissionalResponseDTO));

        List<ProfissionalResponseDTO> profissionais = profissionalService.buscarPorNome("Maria");

        assertThat(profissionais).hasSize(1);
        assertThat(profissionais.get(0).getNome()).isEqualTo("Maria Silva Santos");
        verify(profissionalRepository, times(1)).findByNomeContainingIgnoreCaseAndAtivoTrue("Maria");
        verify(profissionalMapper, times(1)).toResponseDTOList(anyList());
    }

    @Test
    void deveCriarProfissional() {
        when(profissionalRepository.existsByRegistroClasse("CRP 01/12345")).thenReturn(false);
        when(profissionalMapper.toEntity(profissionalRequestDTO)).thenReturn(profissional);
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        when(profissionalMapper.toResponseDTO(profissional)).thenReturn(profissionalResponseDTO);

        ProfissionalResponseDTO criado = profissionalService.criar(profissionalRequestDTO);

        assertThat(criado.getNome()).isEqualTo("Maria Silva Santos");
        assertThat(criado.getRegistroClasse()).isEqualTo("CRP 01/12345");
        verify(profissionalRepository, times(1)).existsByRegistroClasse("CRP 01/12345");
        verify(profissionalMapper, times(1)).toEntity(profissionalRequestDTO);
        verify(especialidadeRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).save(any(Profissional.class));
        verify(profissionalMapper, times(1)).toResponseDTO(profissional);
    }

    @Test
    void deveLancarExcecaoAoCriarProfissionalComRegistroClasseDuplicado() {
        when(profissionalRepository.existsByRegistroClasse("CRP 01/12345")).thenReturn(true);

        assertThrows(BusinessException.class, () -> profissionalService.criar(profissionalRequestDTO));
        verify(profissionalRepository, times(1)).existsByRegistroClasse("CRP 01/12345");
        verify(profissionalRepository, never()).save(any(Profissional.class));
    }

    @Test
    void deveLancarExcecaoAoCriarProfissionalComEspecialidadeInexistente() {
        when(profissionalRepository.existsByRegistroClasse("CRP 01/12345")).thenReturn(false);
        when(profissionalMapper.toEntity(profissionalRequestDTO)).thenReturn(profissional);
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profissionalService.criar(profissionalRequestDTO));
        verify(especialidadeRepository, times(1)).findById(1L);
        verify(profissionalRepository, never()).save(any(Profissional.class));
    }

    @Test
    void deveAtualizarProfissional() {
        ProfissionalUpdateDTO updateDTO = new ProfissionalUpdateDTO();
        updateDTO.setEmail("novoemail@clinica.com");
        updateDTO.setCelular("11999999999");

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        when(profissionalMapper.toResponseDTO(profissional)).thenReturn(profissionalResponseDTO);

        ProfissionalResponseDTO atualizado = profissionalService.atualizar(1L, updateDTO);

        assertThat(atualizado).isNotNull();
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalMapper, times(1)).updateEntityFromDTO(eq(updateDTO), eq(profissional));
        verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    void deveAtualizarProfissionalComNovasEspecialidades() {
        ProfissionalUpdateDTO updateDTO = new ProfissionalUpdateDTO();
        updateDTO.setEspecialidadesIds(Set.of(1L, 2L));

        Especialidade especialidade2 = Especialidade.builder()
                .id(2L)
                .nome("Fonoaudiologia")
                .ativo(true)
                .build();

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(especialidadeRepository.findById(1L)).thenReturn(Optional.of(especialidade));
        when(especialidadeRepository.findById(2L)).thenReturn(Optional.of(especialidade2));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        when(profissionalMapper.toResponseDTO(profissional)).thenReturn(profissionalResponseDTO);

        ProfissionalResponseDTO atualizado = profissionalService.atualizar(1L, updateDTO);

        assertThat(atualizado).isNotNull();
        verify(especialidadeRepository, times(1)).findById(1L);
        verify(especialidadeRepository, times(1)).findById(2L);
        verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    void deveLancarExcecaoAoAtualizarComRegistroClasseDuplicado() {
        ProfissionalUpdateDTO updateDTO = new ProfissionalUpdateDTO();
        updateDTO.setRegistroClasse("CRP 02/99999");

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.existsByRegistroClasse("CRP 02/99999")).thenReturn(true);

        assertThrows(BusinessException.class, () -> profissionalService.atualizar(1L, updateDTO));
        verify(profissionalRepository, times(1)).existsByRegistroClasse("CRP 02/99999");
        verify(profissionalRepository, never()).save(any(Profissional.class));
    }

    @Test
    void deveDesativarProfissional() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        profissionalService.desativar(1L);

        assertThat(profissional.getAtivo()).isFalse();
        verify(profissionalRepository, times(1)).findById(1L);
        verify(profissionalRepository, times(1)).save(profissional);
    }

    @Test
    void deveLancarExcecaoAoDesativarProfissionalInexistente() {
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profissionalService.desativar(99L));
        verify(profissionalRepository, times(1)).findById(99L);
        verify(profissionalRepository, never()).save(any(Profissional.class));
    }

}
