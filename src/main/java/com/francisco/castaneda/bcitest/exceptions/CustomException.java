package com.francisco.castaneda.bcitest.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    private Timestamp timestamp;
    private String message;
    private String detail;
    private HttpStatus status;
}