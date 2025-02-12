package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.repository.EventRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event, @AuthenticationPrincipal User organizer) {
        // L'utente autenticato che crea l'evento deve avere ruolo ORGANIZER
        event.setOrganizer(organizer);
        return eventRepository.save(event);
    }



    // Endpoint per aggiornare un evento
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody Event eventDetails,
                                         @AuthenticationPrincipal User organizer) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // Controlla che l'organizer dell'evento sia lo stesso dell'utente autenticato
                    if (!existingEvent.getOrganizer().getId().equals(organizer.getId())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Non sei autorizzato ad aggiornare questo evento");
                    }
                    // Aggiorna i campi dell'evento
                    existingEvent.setTitle(eventDetails.getTitle());
                    existingEvent.setDescription(eventDetails.getDescription());
                    existingEvent.setEventDate(eventDetails.getEventDate());
                    existingEvent.setLocation(eventDetails.getLocation());
                    existingEvent.setAvailableSeats(eventDetails.getAvailableSeats());
                    // Altri campi
                    eventRepository.save(existingEvent);
                    return ResponseEntity.ok(existingEvent);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Evento non trovato"));
    }

    // Endpoint per eliminare un evento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
                                         @AuthenticationPrincipal User organizer) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // Controlla che l'organizer dell'evento sia lo stesso dell'utente autenticato
                    if (!existingEvent.getOrganizer().getId().equals(organizer.getId())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Non sei autorizzato ad eliminare questo evento");
                    }
                    eventRepository.delete(existingEvent);
                    return ResponseEntity.ok("Evento eliminato con successo");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Evento non trovato"));
    }

}