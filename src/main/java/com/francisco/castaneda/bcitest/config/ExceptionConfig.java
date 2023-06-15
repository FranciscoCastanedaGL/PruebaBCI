package com.francisco.castaneda.bcitest.config;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.exceptions.UserNotFoundException;
import com.francisco.castaneda.bcitest.exceptions.ValidationsException;
import com.francisco.castaneda.bcitest.model.dto.ErrorInfoDTO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class ExceptionConfig {
    @Autowired
    private MapperFacade orikaMapper;

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.EXCEPTION));
            }
        };
    }
    @ExceptionHandler(CustomException.class)
    public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
        res.sendError( ex.getStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfoDTO methodArgumentNotValidException(CustomException ex) {
        ErrorInfoDTO newErrorInfo = orikaMapper.map(ex,ErrorInfoDTO.class );
        newErrorInfo.setCode(ex.getStatus().value());
        return newErrorInfo;
    }

    @ExceptionHandler(ValidationsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorInfoDTO methodArgumentNotValidationException(CustomException ex) {
        ErrorInfoDTO newErrorInfo = orikaMapper.map(ex,ErrorInfoDTO.class );
        newErrorInfo.setCode(ex.getStatus().value());
        return newErrorInfo;
    }
}
