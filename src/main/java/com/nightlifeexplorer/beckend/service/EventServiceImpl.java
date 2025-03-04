package com.nightlifeexplorer.beckend.service;

import com.nightlifeexplorer.beckend.dto.EventDTO;
import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.User;
import com.nightlifeexplorer.beckend.repository.EventRepository;
import com.nightlifeexplorer.beckend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Override
    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO, User organizer) {
        // Verifica esplicita (debug)
//        if (organizer == null) {
//            throw new IllegalStateException("Nessun organizzatore autenticato!");
//        }

        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setLocation(eventDTO.getLocation());
        event.setTicketLink(eventDTO.getTicketLink());
        event.setCategory(eventDTO.getCategory());
        event.setOrganizer(organizer); // Organizer deve essere non null

        Event savedEvent = eventRepository.save(event);
        return mapToDTO(savedEvent);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO, User organizer) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new RuntimeException("Evento non trovato");
        }
        Event event = optionalEvent.get();
//        if (!event.getOrganizer().getId().equals(organizer.getId())) {
//            throw new RuntimeException("Non sei autorizzato ad aggiornare questo evento.");
//        }
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setLocation(eventDTO.getLocation());
        event.setTicketLink(eventDTO.getTicketLink());
        event.setCategory(eventDTO.getCategory());
//        event.setAvailableSeats(eventDTO.getAvailableSeats());
        Event updated = eventRepository.save(event);
        return mapToDTO(updated);
    }

    @Override
    public void deleteEvent(Long id, User organizer) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new RuntimeException("Evento non trovato.");
        }
        Event event = optionalEvent.get();
//        if (!event.getOrganizer().getId().equals(organizer.getId())) {
//            throw new RuntimeException("Non sei autorizzato ad eliminare questo evento.");
//        }
        eventRepository.delete(event);
    }

//    @Override
//    public String saveEvent(Long eventId, String username) {
//        Optional<Event> optionalEvent = eventRepository.findById(eventId);
//        if (optionalEvent.isEmpty()) {
//            return "Evento non trovato.";
//        }
//        Event event = optionalEvent.get();
//        // Presupponendo che user.getSavedEvents() ritorni un Set o List di Event
//        Optional<User> optionalUser = userRepository.findByEmail(username);
//        if (optionalUser.isEmpty()) {
//            return "Utente non trovato.";
//        }
//
//        User user = optionalUser.get();
//
//        if (user.getSavedEvents().contains(event)) {
//            return "Evento già esistente nei preferiti.";
//        }
//
//        user.getSavedEvents().add(event);
//        userRepository.save(user);
//        return null;
//
////        return "Evento salvato con successo.";
//    }

    private EventDTO mapToDTO(Event event) {
//        if (event.getOrganizer() == null) {
//            throw new IllegalStateException("Organizzatore mancante per l'evento con ID: " + event.getId());
//        }

        EventDTO newEvent = new EventDTO();
        newEvent.setId(event.getId());
        newEvent.setTitle(event.getTitle());
        newEvent.setDescription(event.getDescription());
        newEvent.setEventDate(event.getEventDate());
        newEvent.setLocation(event.getLocation());
        newEvent.setTicketLink(event.getTicketLink());
        newEvent.setCategory(event.getCategory());
        if(event.getOrganizer() != null){
            newEvent.setOrganizerUsername(event.getOrganizer().getUsername());
        }

        return newEvent;

//        return new EventDTO(
//                event.getId(),
//                event.getTitle(),
//                event.getDescription(),
//                event.getEventDate(),
//                event.getLocation(),
//                event.getOrganizer().getId(),
//                event.getOrganizer().getUsername()
//        );
    }
}
