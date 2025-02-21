package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.dto.EventDTO;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.service.EventService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // Endpoint pubblico per vedere tutti gli eventi (accessibile anche ai non autenticati)
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Solo gli ORGANIZER possono creare eventi
    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO,
                                         @AuthenticationPrincipal User organizer) {
        System.out.println("Utente autenticato: " + organizer.getEmail());

        EventDTO created = eventService.createEvent(eventDTO, organizer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Solo gli ORGANIZER possono aggiornare i propri eventi
    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody EventDTO eventDTO,
                                         @AuthenticationPrincipal User organizer) {
        EventDTO updated = eventService.updateEvent(id, eventDTO, organizer);
        return ResponseEntity.ok(updated);
    }

    // Solo gli ORGANIZER possono eliminare i propri eventi
    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
                                         @AuthenticationPrincipal User organizer) {
        eventService.deleteEvent(id, organizer);
        return ResponseEntity.ok("Evento eliminato con successo.");
    }

    // Solo gli utenti autenticati con ROLE_USER possono salvare (prenotare) eventi
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/save")
    public ResponseEntity<?> saveEvent(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        String message = eventService.saveEvent(id, user);
        return ResponseEntity.ok(message);
    }


}


