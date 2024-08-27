package br.com.vbruno.minhafeira.controller;

import br.com.vbruno.minhafeira.DTO.response.ErrorResponse;
import br.com.vbruno.minhafeira.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(ProductMarketNotUniqueException.class)
    public ResponseEntity<ErrorResponse> handleProductMarketNotUniqueException(ProductMarketNotUniqueException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ProductInvalidException.class)
    public ResponseEntity<ErrorResponse> handleProductInvalidException(ProductInvalidException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ProductRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleProductRegisteredException(ProductRegisteredException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(CategoryInvalidException.class)
    public ResponseEntity<ErrorResponse> handleCategoryInvalidException(CategoryInvalidException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(CategoryRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleCategoryRegisteredException(CategoryRegisteredException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleUserNotRegisteredException(UserNotRegisteredException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(EmailRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailRegisteredException(EmailRegisteredException ex, HttpServletRequest request) {

        HttpStatus status = UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                              HttpServletRequest request) {
        HttpStatus status = BAD_REQUEST;
        String message = catchError(ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage(message);
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }

    private String catchError(MethodArgumentNotValidException ex) {
        Optional<ObjectError> errorOpt = ex.getBindingResult().getAllErrors().stream()
                .findFirst();

        if (errorOpt.isEmpty()) {
            return "Erro de validação";
        }

        FieldError erro = (FieldError) errorOpt.get();
        return erro.getDefaultMessage();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {

        HttpStatus status = INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setReasonPhrase(status.getReasonPhrase());
        errorResponse.setMessage("Ocorreu algum problema interno no servidor");
        errorResponse.setPath(request.getServletPath());

        return new ResponseEntity<>(errorResponse, status);
    }
}
