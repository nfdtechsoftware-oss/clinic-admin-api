package com.nfdtech.clinic_admin_api.dto.profissional;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome deve ter no máximo 200 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;

    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotBlank(message = "Registro de classe é obrigatório")
    @Size(max = 50, message = "Registro de classe deve ter no máximo 50 caracteres")
    private String registroClasse;

    // Endereço
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 200)
    private String logradouro;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10)
    private String numero;

    @Size(max = 100)
    private String complemento;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser a sigla com 2 letras maiúsculas")
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato XXXXX-XXX")
    private String cep;

    // Contato
    private String telefone;

    private String celular;

    @Email(message = "Email deve ser válido")
    @Size(max = 100)
    private String email;

    // Especialidades
    @NotEmpty(message = "Profissional deve ter pelo menos uma especialidade")
    private Set<Long> especialidadesIds;

}
