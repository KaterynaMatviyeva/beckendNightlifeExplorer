package com.nightlifeexplorer.beckend.service;

import com.nightlifeexplorer.beckend.dto.EventDTO;
import com.nightlifeexplorer.beckend.entity.User;

import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();
    EventDTO createEvent(EventDTO eventDTO, User organizer);
    EventDTO updateEvent(Long id, EventDTO eventDTO, User organizer);
    void deleteEvent(Long id, User organizer);
    String saveEvent(Long eventId, User user);
}