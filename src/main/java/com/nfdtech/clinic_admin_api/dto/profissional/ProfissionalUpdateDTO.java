package com.nfdtech.clinic_admin_api.dto.profissional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalUpdateDTO {

    @Size(max = 200)
    private String nome;

    private LocalDate dataNascimento;

    @Size(max = 50)
    private String registroClasse;

    // Endereço
    @Size(max = 200)
    private String logradouro;

    @Size(max = 10)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser a sigla com 2 letras maiúsculas")
    private String estado;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato XXXXX-XXX")
    private String cep;

    // Contato
    private String telefone;

    private String celular;

    @Email(message = "Email deve ser válido")
    @Size(max = 100)
    private String email;

    // Especialidades
    private Set<Long> especialidadesIds;

    private Boolean ativo;

}
