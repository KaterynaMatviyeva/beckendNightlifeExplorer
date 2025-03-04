package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.dto.EventDTO;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.enums.Role;
import com.nightlifeexplorer.beckend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
//    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO
//            ,@AuthenticationPrincipal User organizer
    ) {

        System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println("Dati ricevuti: " + eventDTO);
//        if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities()
//                .contains(new SimpleGrantedAuthority("ROLE_ORGANIZER"))) {
//            throw new AccessDeniedException("Solo gli organizzatori possono creare eventi");
//        }
//        if (organizer == null || organizer.getRole() != Role.ROLE_ORGANIZER) {
//            throw new AccessDeniedException("Solo gli organizzatori possono creare eventi");
//        }

        EventDTO created = eventService.createEvent(eventDTO, null);
//        EventDTO created = eventService.createEvent(eventDTO, organizer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Solo gli ORGANIZER possono aggiornare i propri eventi
//    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody EventDTO eventDTO
//            ,@AuthenticationPrincipal User organizer
    ) {
//        EventDTO updated = eventService.updateEvent(id, eventDTO, organizer);
        EventDTO updated = eventService.updateEvent(id, eventDTO, null);
        return ResponseEntity.ok(updated);
    }

    // Solo gli ORGANIZER possono eliminare i propri eventi
//    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id
//                                         ,@AuthenticationPrincipal User organizer
    ) {
//        eventService.deleteEvent(id, organizer);
        eventService.deleteEvent(id, null);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //  salvare (prenotare) eventi
//    @PreAuthorize("hasRole('USER')")
//        @PostMapping("/{id}/save")
//        public ResponseEntity<?> saveEvent(@PathVariable Long id
//                                           ,@AuthenticationPrincipal User user
//        ) {
//            String message = "";
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth != null && auth.isAuthenticated()) {
//                Object principal = auth.getPrincipal();
//                if (principal instanceof UserDetails) {
//                    String username = ((UserDetails) principal).getUsername();
//                    message = eventService.saveEvent(id, username);
//                    return message != null ? ResponseEntity.ok(message) : ResponseEntity.ok(HttpStatus.OK);
//                }
//            }
//            return ResponseEntity.ok(HttpStatus.OK);
//        }


}


