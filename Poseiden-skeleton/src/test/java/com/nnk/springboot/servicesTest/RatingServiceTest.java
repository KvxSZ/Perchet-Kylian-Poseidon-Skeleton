package com.nnk.springboot.servicesTest;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRatings() {
        Rating rating1 = new Rating("Moody's A", "S&P A", "Fitch A", 1);
        Rating rating2 = new Rating("Moody's B", "S&P B", "Fitch B", 2);
        List<Rating> ratings = Arrays.asList(rating1, rating2);

        when(ratingRepository.findAll()).thenReturn(ratings);

        List<Rating> result = ratingService.getRatings();

        assertEquals(2, result.size());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void getRatingById() {
        Rating rating = new Rating();
        rating.setMoodysRating("Moody's A");
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.getRatingById(1);

        assertTrue(result.isPresent());
        assertEquals("Moody's A", result.get().getMoodysRating());
        verify(ratingRepository, times(1)).findById(1);
    }

    @Test
    void addRating() {
        Rating rating = new Rating("Moody's A", "S&P A", "Fitch A", 1);

        ratingService.addRating(rating);

        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void updateRating() {
        Rating existingRating = new Rating();
        existingRating.setId(1);
        existingRating.setMoodysRating("Moody's A");
        existingRating.setSandPRating("S&P A");
        existingRating.setFitchRating("Fitch A");
        existingRating.setOrderNumber((byte) 1);
        Rating updatedRating = new Rating();
        updatedRating.setOrderNumber((byte) 2);
        updatedRating.setMoodysRating("Moody's B");
        updatedRating.setSandPRating("S&P B");
        updatedRating.setFitchRating("Fitch B");

        when(ratingRepository.findById(1)).thenReturn(Optional.of(existingRating));

        ratingService.updateRating(1, updatedRating);

        verify(ratingRepository, times(1)).save(existingRating);
        assertEquals("Moody's B", existingRating.getMoodysRating());
        assertEquals("S&P B", existingRating.getSandPRating());
        assertEquals("Fitch B", existingRating.getFitchRating());
        assertEquals((byte) 2, existingRating.getOrderNumber());
    }

    @Test
    void deleteRating() {
        Rating rating = new Rating("Moody's A", "S&P A", "Fitch A", 1);
        rating.setId(1);
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        ratingService.deleteRating(1);

        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void updateRatingNotFound() {
        Rating updatedRating = new Rating("Moody's B", "S&P B", "Fitch B", 2);

        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.updateRating(1, updatedRating);
        });

        String expectedMessage = "Invalid Rating Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteRatingNotFound() {
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.deleteRating(1);
        });

        String expectedMessage = "Invalid Rating Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
