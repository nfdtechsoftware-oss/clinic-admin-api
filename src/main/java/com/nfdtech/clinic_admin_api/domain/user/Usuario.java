package com.nfdtech.clinic_admin_api.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nfdtech.clinic_admin_api.domain.base.Auditavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario extends Auditavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @NotNull
    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

}
