package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.repository.EventRepository;
import com.nightlifeexplorer.beckend.repository.UserRepository;
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

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // Endpoint pubblico per vedere tutti gli eventi (accessibile anche ai non autenticati)
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Solo gli ORGANIZER possono creare eventi
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event,
                                         @AuthenticationPrincipal User organizer) {
        event.setOrganizer(organizer);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    // Solo gli ORGANIZER possono aggiornare i propri eventi
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody Event eventDetails,
                                         @AuthenticationPrincipal User organizer) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    if (!existingEvent.getOrganizer().getId().equals(organizer.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Non sei autorizzato ad aggiornare questo evento.");
                    }
                    existingEvent.setTitle(eventDetails.getTitle());
                    existingEvent.setDescription(eventDetails.getDescription());
                    existingEvent.setEventDate(eventDetails.getEventDate());
                    existingEvent.setLocation(eventDetails.getLocation());
                    existingEvent.setAvailableSeats(eventDetails.getAvailableSeats());
                    eventRepository.save(existingEvent);
                    return ResponseEntity.ok(existingEvent);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Evento non trovato."));
    }

    // Solo gli ORGANIZER possono eliminare i propri eventi
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
                                         @AuthenticationPrincipal User organizer) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    if (!existingEvent.getOrganizer().getId().equals(organizer.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Non sei autorizzato ad eliminare questo evento.");
                    }
                    eventRepository.delete(existingEvent);
                    return ResponseEntity.ok("Evento eliminato con successo.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Evento non trovato."));
    }

    // Solo gli utenti autenticati con ROLE_USER possono salvare (prenotare) eventi
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{id}/save")
    public ResponseEntity<?> saveEvent(@PathVariable Long id,
                                       @AuthenticationPrincipal User user) {
        return eventRepository.findById(id)
                .map(event -> {
                    if (!user.getSavedEvents().contains(event)) {
                        user.getSavedEvents().add(event);
                        userRepository.save(user);
                        return ResponseEntity.ok("Evento salvato con successo.");
                    } else {
                        return ResponseEntity.badRequest().body("Hai già salvato questo evento.");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Evento non trovato."));
    }

    // Se l'utente non è autenticato e prova a salvare un evento, viene reindirizzato alla registrazione
    @GetMapping("/{id}/save")
    public ResponseEntity<?> redirectToRegister() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Devi essere registrato per salvare un evento. Vai alla pagina di registrazione.");
    }
}


