package com.codereggs.superHeroeTeam.controllers;

import com.codereggs.superHeroeTeam.controllers.dtos.AuthenticationRequest;
import com.codereggs.superHeroeTeam.controllers.dtos.AuthenticationResponse;
import com.codereggs.superHeroeTeam.controllers.dtos.UserRegisterRequest;
import com.codereggs.superHeroeTeam.enums.Role;
import com.codereggs.superHeroeTeam.models.User;
import com.codereggs.superHeroeTeam.services.exceptions.NotValidEmailFormat;
import com.codereggs.superHeroeTeam.services.exceptions.UserNotFoundException;
import com.codereggs.superHeroeTeam.services.impl.UserDetailsServiceImpl;
import com.codereggs.superHeroeTeam.util.BindingResultsErrors;
import com.codereggs.superHeroeTeam.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private ModelMapper mapper = new ModelMapper();

    private final static String BAD_CREDENTIALS = "Bad credentials, check the emails and/or the password";

    @PostMapping(path = "/login")
    public ResponseEntity<?> createAthenticationToken(@RequestBody @Valid AuthenticationRequest authenticationRequest, BindingResult bindingResult) {
        ResponseEntity<?> responseEntity;
        if (bindingResult.hasErrors()) { // Se revisa si no hay errores del @Valid.
            responseEntity = BindingResultsErrors.getResponseEntityWithErrors(bindingResult); // Se mandan a traer un ResponseEntity que contenga los errores.
        }
        // Si no hay errores entonces:
        else {
            try {
                // En el userService se verifica que no se esté solicitando un usuario registrado.
                final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
                // Y en el AuthenticationManager que las credenciales sean correctas.
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
                // Si está ok se le asigna un JWT al user.
                final String jwt = jwtUtil.generateToken(userDetails);
                // Y se retorna.
                responseEntity = ResponseEntity.ok(new AuthenticationResponse(jwt));
            } catch (UsernameNotFoundException e) {
                responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (Exception e) {
                responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_CREDENTIALS);
            }
        }
        return responseEntity;
    }

    @PostMapping(path = "/register")
    ResponseEntity<?> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest, BindingResult bindingResult) { // Ocultamos para que no se vea en swagger.
        ResponseEntity<?> response;
        if (bindingResult.hasErrors()) { // Se revisa si no hay errores del @Valid.
            response = BindingResultsErrors.getResponseEntityWithErrors(bindingResult); // Se mandan a traer un ResponseEntity que contenga los errores.
        }
        // Si no hay errores entonces:
        else {
            try {
                // Asignamos el Rol de Usuario Registrado.
                var roleUser = Role.ROLE_USER;
                // Creamos el Usuario a registrar
                var user = mapper.map(userRegisterRequest, User.class);
                // Le agregamos el Role de Usuario registrado.
                user.setRole(roleUser);
                // Guardamos la contraseña sin encriptar para luego poder iniciar sesion una vez registrado.
                var password = user.getPassword();
                // Lo registramos
                userDetailsService.registerUser(user);
                // Creamos la authentication request para poder logearnos en el sistema.
                var authRequest = new AuthenticationRequest(user.getEmail(),password);
                // Iniciamos sesion para recibir el JWT y devolverlo.
                response = createAthenticationToken(authRequest,bindingResult);
            } catch (NotValidEmailFormat ese) {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ese.getMessage());
            } catch (Exception e) {
                response = ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }

        }
        return response;
    }
}
