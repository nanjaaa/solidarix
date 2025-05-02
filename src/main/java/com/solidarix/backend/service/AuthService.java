package com.solidarix.backend.service;

import com.solidarix.backend.dto.AuthRequestDto;
import com.solidarix.backend.dto.AuthResponseDto;
import com.solidarix.backend.dto.RegistrationDto;
import com.solidarix.backend.model.Location;
import com.solidarix.backend.model.Role;
import com.solidarix.backend.model.User;
import com.solidarix.backend.repository.UserRepository;
import com.solidarix.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LocationService locationService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, LocationService locationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.locationService = locationService;
    }

    public AuthResponseDto register(RegistrationDto request){

        // Trouver un adresse ou créer un nouveau
        Location address = locationService.findOrCreateLocation(request.getFullAddress());

        // Construire le User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthday(LocalDate.parse(request.getBirthday()));
        user.setRole(Role.ROLE_USER);
        user.setAddress(address);
        userRepository.save(user);

        System.out.println("Mandalo service");

        // Générer le token
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDto(token, user);
    }

    public AuthResponseDto authenticate(AuthRequestDto request){

        User user = userRepository.findByUsername(request.username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password, user.getPassword())){
            throw new RuntimeException("Incorrect password");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponseDto(token, user);
    }

}
