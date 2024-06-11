package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getRatings() {
        return ratingRepository.findAll();
    }

    public Optional<Rating> getRatingById(Integer id) {
        return ratingRepository.findById(id);
    }

    public void addRating(Rating rating) {
        ratingRepository.save(rating);
    }

    @Transactional
    public void updateRating(Integer id, Rating ratingDetails) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id: " + id));

        rating.setMoodysRating(ratingDetails.getMoodysRating());
        rating.setSandPRating(ratingDetails.getSandPRating());
        rating.setFitchRating(ratingDetails.getFitchRating());
        rating.setOrderNumber(ratingDetails.getOrderNumber());

        ratingRepository.save(rating);
    }

    @Transactional
    public void deleteRating(Integer id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id: " + id));
        ratingRepository.delete(rating);
    }

}
