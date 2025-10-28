package com.nfdtech.clinic_admin_api.repositories;

import com.nfdtech.clinic_admin_api.domain.user.Role;
import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarEBuscarUsuarioPorUsername() {
        Usuario usuario = Usuario.builder()
                .username("testuser")
                .email("testuser@clinic.com")
                .password("123456")
                .role(Role.ADMIN)
                .active(true)
                .build();

        usuarioRepository.save(usuario);
        Optional<Usuario> encontrado = usuarioRepository.findByUsername("testuser");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getUsername()).isEqualTo("testuser");
    }

}
