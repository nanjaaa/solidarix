package com.solidarix.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDto {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    public String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    public String password;

}
