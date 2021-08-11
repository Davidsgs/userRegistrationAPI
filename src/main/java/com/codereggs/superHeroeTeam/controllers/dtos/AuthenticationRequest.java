package com.codereggs.superHeroeTeam.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
