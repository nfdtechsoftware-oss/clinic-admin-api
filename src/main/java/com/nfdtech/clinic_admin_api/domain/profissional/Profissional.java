package com.nfdtech.clinic_admin_api.domain.profissional;

import com.nfdtech.clinic_admin_api.domain.base.Pessoa;
import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Profissional extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registro de classe é obrigatório")
    @Size(max = 50, message = "Registro de classe deve ter no máximo 50 caracteres")
    @Column(name = "registro_classe", nullable = false, unique = true, length = 50)
    private String registroClasse;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "profissionais_especialidades",
            joinColumns = @JoinColumn(name = "profissional_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidade_id")
    )
    @Builder.Default
    private Set<Especialidade> especialidades = new HashSet<>();

    public void adicionarEspecialidade(Especialidade especialidade) {
        this.especialidades.add(especialidade);
    }

    public void removerEspecialidade(Especialidade especialidade) {
        this.especialidades.remove(especialidade);
    }

}
