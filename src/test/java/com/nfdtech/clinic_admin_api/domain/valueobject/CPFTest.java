package com.nfdtech.clinic_admin_api.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

    @Test
    void deveCriarCPFValidoComFormatacao() {
        // CPF válido: 123.456.789-09
        CPF cpf = CPF.of("123.456.789-09");

        assertNotNull(cpf);
        assertEquals("123.456.789-09", cpf.getNumero());
    }

    @Test
    void deveCriarCPFValidoSemFormatacao() {
        // CPF válido sem formatação
        CPF cpf = CPF.of("12345678909");

        assertNotNull(cpf);
        assertEquals("123.456.789-09", cpf.getNumero());
    }

    @Test
    void deveRejeitarCPFComTodosDigitosIguais() {
        assertThrows(IllegalArgumentException.class, () -> {
            CPF.of("111.111.111-11");
        });
    }

    @Test
    void deveRejeitarCPFInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            CPF.of("123.456.789-00");
        });
    }

    @Test
    void deveRejeitarCPFVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            CPF.of("");
        });
    }

    @Test
    void deveRejeitarCPFNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            CPF.of(null);
        });
    }

    @Test
    void deveRejeitarCPFComMenosDe11Digitos() {
        assertThrows(IllegalArgumentException.class, () -> {
            CPF.of("123456789");
        });
    }

    @Test
    void deveRejeitarCPFComMaisDe11Digitos() {
        assertThrows(IllegalArgumentException.class, () -> {
            CPF.of("123456789012");
        });
    }

    @Test
    void deveRetornarCPFFormatadoNoToString() {
        CPF cpf = CPF.of("12345678909");
        assertEquals("123.456.789-09", cpf.toString());
    }
}
