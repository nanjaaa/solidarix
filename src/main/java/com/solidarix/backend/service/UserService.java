package com.solidarix.backend.service;

import com.solidarix.backend.model.User;
import com.solidarix.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id){
        return userRepository.getReferenceById(id);
    }


}
