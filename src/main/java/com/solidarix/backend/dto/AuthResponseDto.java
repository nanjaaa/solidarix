package com.solidarix.backend.dto;

import com.solidarix.backend.model.User;

public class AuthResponseDto {

    public String token;
    public User user;

    public AuthResponseDto(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
