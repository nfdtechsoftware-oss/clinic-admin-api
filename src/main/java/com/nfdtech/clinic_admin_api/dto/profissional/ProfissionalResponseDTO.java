package com.nfdtech.clinic_admin_api.dto.profissional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nfdtech.clinic_admin_api.dto.especialidade.EspecialidadeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfissionalResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String registroClasse;

    // Endereço
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    // Contato
    private String telefone;
    private String celular;
    private String email;

    // Especialidades
    private Set<EspecialidadeResponseDTO> especialidades;

    // Auditoria
    private Boolean ativo;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

}
