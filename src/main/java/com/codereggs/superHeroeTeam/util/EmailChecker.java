package com.codereggs.superHeroeTeam.util;

import java.util.regex.Pattern;

public class EmailChecker {
    public static boolean isValid(String email){
        //Se revisa de que el email tenga la estructura "*@*.*" (* = Cualquier texto), Ejemplo: "algo@email.com".
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if(email == null){
            return false;
        }
        return pat.matcher(email).matches();
    }
}