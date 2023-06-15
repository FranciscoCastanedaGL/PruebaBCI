package com.francisco.castaneda.bcitest.exceptions;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class ValidationsException extends CustomException{
    public ValidationsException(String message, String detail, HttpStatus status) {
        super(new Timestamp(System.currentTimeMillis()), message, detail, status);
    }
}
