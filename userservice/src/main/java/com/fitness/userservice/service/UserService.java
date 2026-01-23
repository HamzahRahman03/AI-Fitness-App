package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserResponse getUserProfile(String userId){
        User user = new User();


    }

    public UserResponse register(@Valid RegisterRequest request) {
         User user = new User();

         user.setEmail(request.getEmail());
         user.setPassword(request.getPassword());
         user.setFirstName(request.getFirstName());
         user.setLastName(request.getLastName());
    }
}
