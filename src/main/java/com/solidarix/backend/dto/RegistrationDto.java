package com.solidarix.backend.dto;

import lombok.Data;

@Data
public class RegistrationDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthday; // mettre au format yyyy-MM-dd
    private String fullAddress; // utilisé pour appeler l'API adresse

}
