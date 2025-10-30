package com.nfdtech.clinic_admin_api.dto.especialidade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadeResponseDTO {

    private Long id;

    private String nome;

    private String descricao;

    private Boolean ativo;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;

}
