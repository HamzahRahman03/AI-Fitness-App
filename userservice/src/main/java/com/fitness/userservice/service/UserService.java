package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserResponse getUserProfile(String userId){
        User user = new User();

        user.getId();
        user.getEmail();
        user.getPassword();
        user.getFirstName();
        user.getLastName();
        user.getCreatedAt();
        user.getUpdatedAt();
    }

    @Autowired
    UserRepository userRepository;

    public UserResponse register(@Valid RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User already exists");
        }

        User user = new User();
         user.setEmail(request.getEmail());
         user.setPassword(request.getPassword());
         user.setFirstName(request.getFirstName());
         user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;

    }
}
