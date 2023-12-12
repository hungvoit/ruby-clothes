package com.javaguides.clothesbabies.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;


@ControllerAdvice
public class ControllerErrorAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnsupportedOperationException.class)
    public String handleErrorUnsupportedOperationException(UnsupportedOperationException exception) {
        System.out.print(exception.getMessage());
        return "/403";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleErrorNoSuchElementException(NoSuchElementException exception) {
        System.out.print(exception.getMessage());
        return "/403";
    }

}
