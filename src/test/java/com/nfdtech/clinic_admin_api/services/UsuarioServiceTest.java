package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.user.Role;
import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import com.nfdtech.clinic_admin_api.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("natalia");
        usuario.setEmail("natalia@clinic.com");
        usuario.setPassword("senha123");
        usuario.setRole(Role.ADMIN);
        usuario.setActive(true);
    }

    @Test
    void deveListarApenasUsuariosAtivos() {
        when(usuarioRepository.findByActiveTrue()).thenReturn(List.of(usuario));

        List<Usuario> usuarios = usuarioService.listarTodos();

        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).isActive()).isTrue();
        verify(usuarioRepository, times(1)).findByActiveTrue();
    }

    @Test
    void deveSalvarUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario salvo = usuarioService.salvar(usuario);

        assertThat(salvo.getUsername()).isEqualTo("natalia");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorId(1L);

        assertThat(encontrado.getEmail()).isEqualTo("natalia@clinic.com");
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void deveDesativarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.desativar(1L);

        assertThat(usuario.isActive()).isFalse();
        verify(usuarioRepository, times(1)).save(usuario);
    }

}
