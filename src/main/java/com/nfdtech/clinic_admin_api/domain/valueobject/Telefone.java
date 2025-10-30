package com.nfdtech.clinic_admin_api.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class Telefone {

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}",
             message = "Telefone deve estar no formato (XX) XXXXX-XXXX ou (XX) XXXX-XXXX")
    private String numero;

    public static Telefone of(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            return null;
        }

        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");

        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            throw new IllegalArgumentException("Telefone deve conter 10 ou 11 dígitos");
        }

        String telefoneFormatado;
        if (telefoneLimpo.length() == 10) {
            // (XX) XXXX-XXXX
            telefoneFormatado = String.format("(%s) %s-%s",
                telefoneLimpo.substring(0, 2),
                telefoneLimpo.substring(2, 6),
                telefoneLimpo.substring(6, 10)
            );
        } else {
            // (XX) XXXXX-XXXX
            telefoneFormatado = String.format("(%s) %s-%s",
                telefoneLimpo.substring(0, 2),
                telefoneLimpo.substring(2, 7),
                telefoneLimpo.substring(7, 11)
            );
        }

        return new Telefone(telefoneFormatado);
    }

    @Override
    public String toString() {
        return numero;
    }
}
