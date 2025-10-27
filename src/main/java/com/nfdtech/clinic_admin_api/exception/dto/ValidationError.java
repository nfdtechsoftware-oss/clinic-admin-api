package com.nfdtech.clinic_admin_api.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValidationError implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String field;

    private String message;

}
