package com.nightlifeexplorer.beckend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(length = 500)
    private String ticketLink;  // Facoltativo

    @Column(length = 50)
    private String category;

//    @Min(1)
//    @Column(nullable = false)
//    private int availableSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
}
