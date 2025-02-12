package com.nightlifeexplorer.beckend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Il nome utente è obbligatorio")
    private String username;

    @Email(message = "Deve essere un indirizzo email valido")
    @NotBlank(message = "L'email è obbligatoria")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, max = 20, message = "La password deve essere compresa tra 6 e 20 caratteri")
    private String password;
}
