package com.solidarix.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationDto {

    @NotBlank(message = "Le nom d'utilisateur est obliagtoire")
    private String username;

    @NotBlank(message = "L'adresse mail est obligatoire")
    @Email
    private String email;

    @NotBlank(message = "Le mot de passe est obliagtoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au mions 6 caractères")
    private String password;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate birthday; // mettre au format yyyy-MM-dd

    private LocationDto address;

}
