package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.user.Usuario;
import com.nfdtech.clinic_admin_api.dto.user.UsuarioRequestDTO;
import com.nfdtech.clinic_admin_api.dto.user.UsuarioResponseDTO;
import com.nfdtech.clinic_admin_api.dto.user.UsuarioUpdateDTO;
import com.nfdtech.clinic_admin_api.exception.custom.ResourceNotFoundException;
import com.nfdtech.clinic_admin_api.mapper.UsuarioMapper;
import com.nfdtech.clinic_admin_api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        List<Usuario> usuarios = usuarioRepository.findByActiveTrue();
        return usuarioMapper.toResponseDTOList(usuarios);
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioMapper.toEntity(requestDTO);

        // Criptografar senha antes de salvar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO updateDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // Criptografar senha se foi fornecida
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            updateDTO.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        usuarioMapper.updateEntityFromDTO(updateDTO, usuario);
        Usuario updated = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(updated);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        usuario.setActive(false);
        usuarioRepository.save(usuario);
    }

}
