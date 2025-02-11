package com.nightlifeexplorer.beckend.controller;

import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.Review;
import com.nightlifeexplorer.beckend.service.ReviewService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @GetMapping("/event/{eventId}")
    public List<Review> getReviewsByEvent(@PathVariable Long eventId) {
        // per poter recuperare l'evento dal repository per validare l'id, per semplicità qui assumiamo che l'evento sia valido
        Event event = new Event();
        event.setId(eventId);
        return reviewService.getReviewsForEvent(event);
    }
}
