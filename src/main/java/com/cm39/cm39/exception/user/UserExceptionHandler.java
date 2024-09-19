package com.cm39.cm39.exception.user;

import com.cm39.cm39.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.cm39.cm39.user.controller")
public class UserExceptionHandler {

    // validation 관련 예외
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // token 관련 예외
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenExeption(TokenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // user 관련 예외
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // custom 예외 최상위
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleBaseException(BaseException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
