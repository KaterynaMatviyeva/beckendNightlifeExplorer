package com.nightlifeexplorer.beckend.service;


import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.Photo;
import com.nightlifeexplorer.beckend.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public Photo uploadPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> getPhotosForEvent(Event event) {
        return photoRepository.findByEvent(event);
    }
}