package com.nfdtech.clinic_admin_api.repositories;

import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByActiveTrue();

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

}
