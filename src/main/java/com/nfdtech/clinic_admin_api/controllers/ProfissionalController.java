package com.nfdtech.clinic_admin_api.controllers;

import com.nfdtech.clinic_admin_api.dto.profissional.*;
import com.nfdtech.clinic_admin_api.services.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCAO')")
    public ResponseEntity<List<ProfissionalResponseDTO>> listar() {
        return ResponseEntity.ok(profissionalService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCAO', 'MEDICO')")
    public ResponseEntity<ProfissionalResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.buscarPorId(id));
    }

    @GetMapping("/especialidade/{especialidadeId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCAO')")
    public ResponseEntity<List<ProfissionalResponseDTO>> buscarPorEspecialidade(
            @PathVariable Long especialidadeId) {
        return ResponseEntity.ok(profissionalService.buscarPorEspecialidade(especialidadeId));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCAO')")
    public ResponseEntity<List<ProfissionalResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(profissionalService.buscarPorNome(nome));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProfissionalResponseDTO> criar(@Valid @RequestBody ProfissionalRequestDTO requestDTO) {
        ProfissionalResponseDTO response = profissionalService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProfissionalResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfissionalUpdateDTO updateDTO) {
        return ResponseEntity.ok(profissionalService.atualizar(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        profissionalService.desativar(id);
        return ResponseEntity.noContent().build();
    }

}
