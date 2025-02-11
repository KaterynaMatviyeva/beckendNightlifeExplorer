package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.Photo;
import com.nightlifeexplorer.beckend.service.PhotoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    public Photo uploadPhoto(@RequestBody Photo photo) {
        return photoService.uploadPhoto(photo);
    }

    @GetMapping("/event/{eventId}")
    public List<Photo> getPhotosByEvent(@PathVariable Long eventId) {
        Event event = new Event();
        event.setId(eventId);
        return photoService.getPhotosForEvent(event);
    }
}