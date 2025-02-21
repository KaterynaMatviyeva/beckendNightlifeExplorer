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
        Event event = new Event();
        event.setTitle(eventDTO.title());
        event.setDescription(eventDTO.description());
        event.setEventDate(eventDTO.eventDate());
        event.setLocation(eventDTO.location());
        event.setAvailableSeats(eventDTO.availableSeats());
        event.setOrganizer(organizer);
        Event saved = eventRepository.save(event);
        return mapToDTO(saved);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO, User organizer) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            throw new RuntimeException("Evento non trovato");
        }
        Event event = optionalEvent.get();
        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new RuntimeException("Non sei autorizzato ad aggiornare questo evento.");
        }
        event.setTitle(eventDTO.title());
        event.setDescription(eventDTO.description());
        event.setEventDate(eventDTO.eventDate());
        event.setLocation(eventDTO.location());
        event.setAvailableSeats(eventDTO.availableSeats());
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
        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new RuntimeException("Non sei autorizzato ad eliminare questo evento.");
        }
        eventRepository.delete(event);
    }

    @Override
    public String saveEvent(Long eventId, User user) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            throw new RuntimeException("Evento non trovato.");
        }
        Event event = optionalEvent.get();
        // Presupponendo che user.getSavedEvents() ritorni un Set o List di Event
        if (user.getSavedEvents().contains(event)) {
            return "Hai già salvato questo evento.";
        }
        user.getSavedEvents().add(event);
        userRepository.save(user);
        return "Evento salvato con successo.";
    }

    private EventDTO mapToDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getLocation(),
                event.getAvailableSeats(),
                event.getOrganizer().getId(),
                event.getOrganizer().getUsername()
        );
    }
}
