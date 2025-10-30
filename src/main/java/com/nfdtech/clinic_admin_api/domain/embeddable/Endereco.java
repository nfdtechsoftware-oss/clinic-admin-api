package com.nfdtech.clinic_admin_api.domain.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Componente de endereço que pode ser incorporado em entidades.
 * Preparado para integração futura com API ViaCEP dos Correios.
 *
 * Uso futuro: EnderecoService.buscarPorCep("01310-100")
 * retornará um Endereco pré-preenchido com dados dos Correios.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato XXXXX-XXX")
    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 200, message = "Logradouro deve ter no máximo 200 caracteres")
    @Column(name = "logradouro", length = 200)
    private String logradouro;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    @Column(name = "numero", length = 10)
    private String numero;

    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    @Column(name = "complemento", length = 100)
    private String complemento;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    @Column(name = "bairro", length = 100)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    @Column(name = "cidade", length = 100)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser a sigla com 2 letras maiúsculas")
    @Column(name = "estado", length = 2)
    private String estado;

    /**
     * Método auxiliar para criar endereço a partir de dados do ViaCEP.
     * Uso futuro quando implementar a integração com API dos Correios.
     *
     * @param cep CEP no formato 00000-000
     * @param numero Número do imóvel (fornecido pelo usuário)
     * @param complemento Complemento opcional
     * @return Endereco com dados parcialmente preenchidos (faltando apenas número/complemento)
     */
    public static Endereco fromCep(String cep, String logradouro, String bairro,
                                   String cidade, String estado, String numero, String complemento) {
        return Endereco.builder()
                .cep(cep)
                .logradouro(logradouro)
                .bairro(bairro)
                .cidade(cidade)
                .estado(estado)
                .numero(numero)
                .complemento(complemento)
                .build();
    }

    /**
     * Formata CEP removendo caracteres não numéricos e adicionando hífen.
     *
     * @param cep CEP com ou sem formatação
     * @return CEP formatado (00000-000)
     */
    public static String formatarCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }

        String cepLimpo = cep.replaceAll("[^0-9]", "");

        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos");
        }

        return String.format("%s-%s",
            cepLimpo.substring(0, 5),
            cepLimpo.substring(5, 8)
        );
    }
}
