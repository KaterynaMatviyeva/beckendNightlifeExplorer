package com.nightlifeexplorer.beckend.service;

import com.nightlifeexplorer.beckend.entity.Event;
import com.nightlifeexplorer.beckend.entity.Review;
import com.nightlifeexplorer.beckend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForEvent(Event event) {
        return reviewRepository.findByEvent(event);
    }
}
