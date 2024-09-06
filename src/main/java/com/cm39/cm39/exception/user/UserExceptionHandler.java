package com.cm39.cm39.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// user controller 관리
@ControllerAdvice(basePackages = "com.cm39.cm39.user.controller")
public class UserExceptionHandler {

    // 이미 존재하는 계정
    @ExceptionHandler(AlreadyExistsUserException.class)
    public ResponseEntity<String> handleAlreadyExistsUserException(AlreadyExistsUserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 필수정보입력 누락
    @ExceptionHandler(EmptyRequiredInfoException.class)
    public ResponseEntity<String> handleEmptyRequiredInfoException(EmptyRequiredInfoException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 유효하지 않은 날짜
    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDateException(InvalidDateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 유효하지 않은 입력 형식
    @ExceptionHandler(InvalidInputFormatException.class)
    public ResponseEntity<String> handleInvalidInputFormatException(InvalidInputFormatException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 보안이 취약한 비밀번호
    @ExceptionHandler(PasswordTooWeakException.class)
    public ResponseEntity<String> handlePasswordTooWeakException(PasswordTooWeakException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 찾을 수 없는 회원
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 인증 실패
    @ExceptionHandler(UserVerifyException.class)
    public ResponseEntity<String> handleUserVerifyException(UserVerifyException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 이메일 전송 실패
    @ExceptionHandler(FailSendEmailException.class)
    public ResponseEntity<String> handleFailSendEmailException(FailSendEmailException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 회원에서 발생하는 최상위 예외
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
