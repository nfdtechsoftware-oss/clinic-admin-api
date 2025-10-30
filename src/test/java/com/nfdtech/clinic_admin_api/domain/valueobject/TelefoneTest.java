package com.nfdtech.clinic_admin_api.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelefoneTest {

    @Test
    void deveCriarTelefoneFixoComFormatacao() {
        Telefone telefone = Telefone.of("(11) 1234-5678");

        assertNotNull(telefone);
        assertEquals("(11) 1234-5678", telefone.getNumero());
    }

    @Test
    void deveCriarTelefoneFixoSemFormatacao() {
        Telefone telefone = Telefone.of("1112345678");

        assertNotNull(telefone);
        assertEquals("(11) 1234-5678", telefone.getNumero());
    }

    @Test
    void deveCriarTelefoneCelularComFormatacao() {
        Telefone telefone = Telefone.of("(11) 91234-5678");

        assertNotNull(telefone);
        assertEquals("(11) 91234-5678", telefone.getNumero());
    }

    @Test
    void deveCriarTelefoneCelularSemFormatacao() {
        Telefone telefone = Telefone.of("11912345678");

        assertNotNull(telefone);
        assertEquals("(11) 91234-5678", telefone.getNumero());
    }

    @Test
    void deveRetornarNuloParaTelefoneVazio() {
        Telefone telefone = Telefone.of("");

        assertNull(telefone);
    }

    @Test
    void deveRetornarNuloParaTelefoneNulo() {
        Telefone telefone = Telefone.of(null);

        assertNull(telefone);
    }

    @Test
    void deveRejeitarTelefoneComMenosDe10Digitos() {
        assertThrows(IllegalArgumentException.class, () -> {
            Telefone.of("123456789");
        });
    }

    @Test
    void deveRejeitarTelefoneComMaisDe11Digitos() {
        assertThrows(IllegalArgumentException.class, () -> {
            Telefone.of("123456789012");
        });
    }

    @Test
    void deveRetornarTelefoneFormatadoNoToString() {
        Telefone telefone = Telefone.of("11912345678");
        assertEquals("(11) 91234-5678", telefone.toString());
    }

    @Test
    void deveAceitarTelefoneComCaracteresEspeciais() {
        Telefone telefone = Telefone.of("(11) 9 1234-5678");

        assertNotNull(telefone);
        assertEquals("(11) 91234-5678", telefone.getNumero());
    }
}
