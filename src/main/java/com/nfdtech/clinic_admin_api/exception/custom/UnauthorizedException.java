package com.nfdtech.clinic_admin_api.exception.custom;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

}
