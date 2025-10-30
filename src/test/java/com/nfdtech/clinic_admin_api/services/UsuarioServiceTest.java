package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.user.Role;
import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import com.nfdtech.clinic_admin_api.dto.user.UsuarioRequestDTO;
import com.nfdtech.clinic_admin_api.dto.user.UsuarioResponseDTO;
import com.nfdtech.clinic_admin_api.dto.user.UsuarioUpdateDTO;
import com.nfdtech.clinic_admin_api.mapper.UsuarioMapper;
import com.nfdtech.clinic_admin_api.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder()
                .id(1L)
                .username("natalia")
                .email("natalia@clinic.com")
                .password("$2a$10$hashedPassword")
                .role(Role.ADMIN)
                .active(true)
                .build();

        usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(1L);
        usuarioResponseDTO.setUsername("natalia");
        usuarioResponseDTO.setEmail("natalia@clinic.com");
        usuarioResponseDTO.setRole(Role.ADMIN);
        usuarioResponseDTO.setActive(true);
    }

    @Test
    void deveListarApenasUsuariosAtivos() {
        when(usuarioRepository.findByActiveTrue()).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponseDTOList(anyList())).thenReturn(List.of(usuarioResponseDTO));

        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();

        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).isActive()).isTrue();
        assertThat(usuarios.get(0).getUsername()).isEqualTo("natalia");
        verify(usuarioRepository, times(1)).findByActiveTrue();
        verify(usuarioMapper, times(1)).toResponseDTOList(anyList());
    }

    @Test
    void deveSalvarUsuario() {
        usuarioRequestDTO = new UsuarioRequestDTO();
        usuarioRequestDTO.setUsername("natalia");
        usuarioRequestDTO.setEmail("natalia@clinic.com");
        usuarioRequestDTO.setPassword("senha123");
        usuarioRequestDTO.setRole(Role.ADMIN);

        Usuario usuarioSemHash = Usuario.builder()
                .id(1L)
                .username("natalia")
                .email("natalia@clinic.com")
                .password("senha123")
                .role(Role.ADMIN)
                .active(true)
                .build();

        when(usuarioMapper.toEntity(usuarioRequestDTO)).thenReturn(usuarioSemHash);
        when(passwordEncoder.encode("senha123")).thenReturn("$2a$10$hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO salvo = usuarioService.criar(usuarioRequestDTO);

        assertThat(salvo.getUsername()).isEqualTo("natalia");
        assertThat(salvo.getEmail()).isEqualTo("natalia@clinic.com");
        verify(usuarioMapper, times(1)).toEntity(usuarioRequestDTO);
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(usuarioMapper, times(1)).toResponseDTO(usuario);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO encontrado = usuarioService.buscarPorId(1L);

        assertThat(encontrado.getEmail()).isEqualTo("natalia@clinic.com");
        assertThat(encontrado.getUsername()).isEqualTo("natalia");
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioMapper, times(1)).toResponseDTO(usuario);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(99L));
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    void deveAtualizarUsuario() {
        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO();
        updateDTO.setEmail("novoemail@clinic.com");
        updateDTO.setPassword("novaSenha123");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("$2a$10$newHashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO atualizado = usuarioService.atualizar(1L, updateDTO);

        assertThat(atualizado).isNotNull();
        verify(usuarioRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("novaSenha123");
        verify(usuarioMapper, times(1)).updateEntityFromDTO(any(UsuarioUpdateDTO.class), eq(usuario));
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveDesativarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.desativar(1L);

        assertThat(usuario.isActive()).isFalse();
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuario);
    }

}
