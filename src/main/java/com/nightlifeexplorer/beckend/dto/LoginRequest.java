package com.nightlifeexplorer.beckend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


public record LoginRequest (
    @NotNull(message = "Email è obbligatorio")
    @NotBlank(message = "Email è obbligatoria")
    String email,

    @NotNull(message = "La password è obbligatoria")
    @NotBlank(message = "La password è obbligatoria")
    String password
){

}
