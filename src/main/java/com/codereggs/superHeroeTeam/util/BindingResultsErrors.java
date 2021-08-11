package com.codereggs.superHeroeTeam.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Stream;

public class BindingResultsErrors {

    public static Stream<String> getErrors(BindingResult bindingResult){
        return bindingResult.getFieldErrors() //Buscamos los errores.
                .stream()// Hacemos un stream para acceder a los distintos campos
                .map(x -> x.getField() + ": " + x.getDefaultMessage());// Convertimos los campos a uns String diciendo el campo con el error.

    }

    public static ResponseEntity<?> getResponseEntityWithErrors(BindingResult bindingResult){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrors(bindingResult));
    }
}