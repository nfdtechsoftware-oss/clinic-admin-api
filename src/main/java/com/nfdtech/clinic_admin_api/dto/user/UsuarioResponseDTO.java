package com.nfdtech.clinic_admin_api.dto.user;

import com.nfdtech.clinic_admin_api.domain.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de resposta do usuário")
public class UsuarioResponseDTO {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private boolean active;

    private String createdBy;

    private LocalDateTime createdAt;

    private String updatedBy;

    private LocalDateTime updatedAt;

}
