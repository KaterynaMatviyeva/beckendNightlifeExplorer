package com.nightlifeexplorer.beckend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long id;

    @NotBlank(message = "Il titolo è obbligatorio")
    private String title;

    private String description;


    @NotNull(message = "La data è obbligatoria")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate eventDate;


    private String location;
    //    private Integer availableSeats;

    private String ticketLink;  // non obbligatorio
    private String category;

    private Long organizerId;
    private String organizerUsername;
}

