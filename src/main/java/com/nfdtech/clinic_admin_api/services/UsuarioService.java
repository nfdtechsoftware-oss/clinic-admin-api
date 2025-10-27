package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import com.nfdtech.clinic_admin_api.exception.custom.ResourceNotFoundException;
import com.nfdtech.clinic_admin_api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        // Retorna apenas usuários ativos
        return usuarioRepository.findByActiveTrue();
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        usuario.setActive(false);
        usuarioRepository.save(usuario);
    }

}
