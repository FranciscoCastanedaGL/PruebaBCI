package com.francisco.castaneda.bcitest.exceptions.exceptionhandler;

import com.francisco.castaneda.bcitest.exceptions.CustomException;
import com.francisco.castaneda.bcitest.exceptions.UserNotFoundException;
import com.francisco.castaneda.bcitest.exceptions.ValidationsException;
import com.francisco.castaneda.bcitest.mapper.ExceptionMapper;
import com.francisco.castaneda.bcitest.model.dto.ErrorInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionConfig {

    private final ExceptionMapper exceptionMapper;

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
        ErrorInfoDTO newErrorInfo = exceptionMapper.mapToErrorInfoDTO(ex,ErrorInfoDTO.class );
        newErrorInfo.setCode(ex.getStatus().value());
        return newErrorInfo;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorInfoDTO> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        return result.getFieldErrors().stream()
                .map(this::buildErrorMessage)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + ex.getMessage());
    }

    private ErrorInfoDTO buildErrorMessage(FieldError fieldError) {
        String defaultMessage = fieldError.getDefaultMessage();
        ErrorInfoDTO newErrorInfo = new ErrorInfoDTO();
        newErrorInfo.setCode(HttpStatus.BAD_REQUEST.value());
        newErrorInfo.setMessage(defaultMessage);
        newErrorInfo.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return newErrorInfo;
    }
}
