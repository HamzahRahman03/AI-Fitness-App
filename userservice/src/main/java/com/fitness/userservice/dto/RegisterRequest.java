package com.fitness.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "email cannot be blank")
    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, message = "password should contain more than 8 characters")
    private String password;

    private String firstName;
    private String lastName;
}
