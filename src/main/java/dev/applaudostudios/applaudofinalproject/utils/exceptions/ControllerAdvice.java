package dev.applaudostudios.applaudofinalproject.utils.exceptions;

import dev.applaudostudios.applaudofinalproject.dto.responses.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = MyBusinessException.class)
    public ResponseEntity<ErrorDto> businessExceptionHandler(MyBusinessException ex) {
        List<String> listOfCustomErrors = new ArrayList<>();
        listOfCustomErrors.add(ex.getMessage());
        ErrorDto err = ErrorDto.builder()
                .errors(listOfCustomErrors)
                .httpStatus(ex.getHttpStatus())
                .build();

        return new ResponseEntity<>(err, ex.getHttpStatus());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorDto> authenticationExceptionExceptionHandler(Exception ex) {
        List<String> listOfAuthErrors = new ArrayList<>();
        listOfAuthErrors.add(ex.getMessage());
        ErrorDto err = ErrorDto.builder()
                .errors(listOfAuthErrors)
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();

        return new ResponseEntity<>(err, err.getHttpStatus());
    }

    @ExceptionHandler(value = HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorDto> httpClientErrorUnauthorizedExceptionHandler(HttpClientErrorException.Unauthorized ex) {
        List<String> listOfClientErrors = new ArrayList<>();

        listOfClientErrors.add("Invalid User Credentials");
        ErrorDto err = ErrorDto.builder()
                .errors(listOfClientErrors)
                .httpStatus(ex.getStatusCode())
                .build();

        return new ResponseEntity<>(err, err.getHttpStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> constraintExceptionHandler(ConstraintViolationException ex) {
        List<String> listOfClientErrors = new ArrayList<>();
        listOfClientErrors.add(ex.getMessage().split(":")[1].trim());
        ErrorDto err = ErrorDto.builder()
                .errors(listOfClientErrors)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(err, err.getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> defaultExceptionHandler(MethodArgumentTypeMismatchException ex) {
        List<String> listOfClientErrors = new ArrayList<>();
        listOfClientErrors.add(ex.getMessage());
        ErrorDto err = ErrorDto.builder()
                .errors(listOfClientErrors)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(err, err.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidateException(MethodArgumentNotValidException ex) {
        List<String> listOfMethodErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            listOfMethodErrors.add(error.getDefaultMessage());
        });
        ErrorDto err = ErrorDto.builder()
                .errors(listOfMethodErrors)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }


}
