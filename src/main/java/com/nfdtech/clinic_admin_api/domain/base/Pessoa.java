package com.nfdtech.clinic_admin_api.domain.base;

import com.nfdtech.clinic_admin_api.domain.embeddable.Contato;
import com.nfdtech.clinic_admin_api.domain.embeddable.Endereco;
import com.nfdtech.clinic_admin_api.domain.valueobject.CPF;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Pessoa extends Auditavel {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Embedded
    @NotNull(message = "CPF é obrigatório")
    @AttributeOverrides({
        @AttributeOverride(name = "numero", column = @Column(name = "cpf", unique = true, nullable = false, length = 14))
    })
    private CPF cpf;

    @Past(message = "Data de nascimento deve ser no passado")
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Embedded
    @Valid
    private Endereco endereco;

    @Embedded
    @Valid
    private Contato contato;

    @NotNull
    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
