package org.example.springboot_car.controller;

import org.example.springboot_car.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${error.message}")
    private String errorMessage;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleArgMethodNotValidException(MethodArgumentNotValidException exception, Model model) {
        List<String> textFromError = new ArrayList<>();
        for (ObjectError message : exception.getBindingResult().getAllErrors()) {
            textFromError.add(message.getDefaultMessage());
        }
        model.addAttribute("listMessageForUser", textFromError);
        return "errors/validationError";
    }

    @ExceptionHandler(Exception.class)
    public String handleCommonException(Exception exception, Model model) {
        model.addAttribute("messageForUser", errorMessage);
        return "errors/errorPage";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException exception, Model model) {
        model.addAttribute("messageForUser", exception.getMessage());
        return "errors/errorPage";
    }

}
