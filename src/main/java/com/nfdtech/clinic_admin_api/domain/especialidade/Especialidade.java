package com.nfdtech.clinic_admin_api.domain.especialidade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nfdtech.clinic_admin_api.domain.base.Auditavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "especialidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Especialidade extends Auditavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
