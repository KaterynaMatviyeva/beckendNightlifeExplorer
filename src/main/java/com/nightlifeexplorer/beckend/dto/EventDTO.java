package com.nightlifeexplorer.beckend.dto;

import java.time.LocalDateTime;

public record EventDTO(
        Long id,
        String title,
        String description,
        LocalDateTime eventDate,
        String location,
        int availableSeats,
        Long organizerId,
        String organizerUsername
) {}