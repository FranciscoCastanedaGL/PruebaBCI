package com.francisco.castaneda.bcitest.exceptions;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException() {
        super(new Timestamp(System.currentTimeMillis()),"USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND);
}
}
