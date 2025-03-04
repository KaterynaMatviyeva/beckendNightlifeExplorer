package com.nightlifeexplorer.beckend.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Email(message = "Email non valida")
    private String email;

    private String username;

    private String password;

    private String role;
}
