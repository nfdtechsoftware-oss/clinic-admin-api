package com.nfdtech.clinic_admin_api.exception.custom;

import com.nfdtech.clinic_admin_api.exception.dto.ValidationError;
import lombok.Getter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<ValidationError> validationErrors;

    public ValidationException(String message) {
        super(message);
        this.validationErrors = new ArrayList<>();
    }

    public ValidationException(String message, List<ValidationError> validationErrors) {
        super(message);
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public ValidationException(String field, String message) {
        super("Erro de validação");
        this.validationErrors = new ArrayList<>();
        this.validationErrors.add(new ValidationError(field, message));
    }
}
