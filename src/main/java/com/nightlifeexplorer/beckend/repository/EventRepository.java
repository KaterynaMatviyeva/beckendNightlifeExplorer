package com.nightlifeexplorer.beckend.repository;

import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Trova eventi organizzati da uno specifico organizzatore
    List<Event> findByOrganizer(User organizer);

    // Trova eventi per località
    List<Event> findByLocationContainingIgnoreCase(String location);

    // Trova eventi dopo una certa data
    List<Event> findByEventDateAfter(LocalDate date);

    // Trova eventi tra due date specifiche
    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);

    // Trova eventi con un titolo specifico o simile
    List<Event> findByTitleContainingIgnoreCase(String title);

    // Trova eventi con posti disponibili
//    List<Event> findByAvailableSeatsGreaterThan(int minSeats);
}