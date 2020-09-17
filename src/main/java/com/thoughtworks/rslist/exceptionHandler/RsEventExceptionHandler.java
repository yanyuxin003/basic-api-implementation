package com.thoughtworks.rslist.exceptionHandler;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.domain.RsEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RsEventExceptionHandler {

    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler - Detail: %s";
    private static Logger log = LoggerFactory.getLogger(RsEventExceptionHandler.class);

    @ExceptionHandler(value = {RsEventNotValidException.class, MethodArgumentNotValidException.class, IndexOutOfBoundsException.class})
    public ResponseEntity reExceptionHandler(Exception e){
        String errorMessage = null;
        if(e instanceof RsEventNotValidException) {
            errorMessage = e.getMessage();
        } else if (((MethodArgumentNotValidException) e).getBindingResult().getTarget() instanceof RsEvent)
            errorMessage = "there is a message error!";
        Error error = new Error();
        error.setError(errorMessage);

        log.error(String.format(logExceptionFormat, errorMessage));
        return ResponseEntity.badRequest().body(error);
    }
}