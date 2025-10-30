package com.nfdtech.clinic_admin_api.controllers;

import com.nfdtech.clinic_admin_api.dto.especialidade.*;
import com.nfdtech.clinic_admin_api.services.EspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCAO', 'MEDICO')")
    public ResponseEntity<List<EspecialidadeResponseDTO>> listar() {
        return ResponseEntity.ok(especialidadeService.listarTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RECEPCAO', 'MEDICO')")
    public ResponseEntity<EspecialidadeResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EspecialidadeResponseDTO> criar(@Valid @RequestBody EspecialidadeRequestDTO requestDTO) {
        EspecialidadeResponseDTO response = especialidadeService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EspecialidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EspecialidadeUpdateDTO updateDTO) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        especialidadeService.desativar(id);
        return ResponseEntity.noContent().build();
    }

}
