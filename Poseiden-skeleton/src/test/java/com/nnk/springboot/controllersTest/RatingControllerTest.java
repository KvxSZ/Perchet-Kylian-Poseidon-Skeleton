package com.nnk.springboot.controllersTest;

import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RatingControllerTest {

    @InjectMocks
    private RatingController ratingController;

    @Mock
    private RatingService ratingService;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
    }

    @Test
    void home() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Rating rating1 = new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10);
        Rating rating2 = new Rating("Moodys Rating 2", "Sand PRating 2", "Fitch Rating 2", 20);
        when(ratingService.getRatings()).thenReturn(Arrays.asList(rating1, rating2));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", hasSize(2)))
                .andExpect(model().attribute("ratings", is(Arrays.asList(rating1, rating2))));

        verify(ratingService, times(1)).getRatings();
    }

    @Test
    void addRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    void validate() throws Exception {
        Rating rating = new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/rating/validate")
                        .flashAttr("rating", rating)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        verify(ratingService, times(1)).addRating(any(Rating.class));
    }


    @Test
    void showUpdateForm() throws Exception {
        Rating rating = new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10);
        when(ratingService.getRatingById(anyInt())).thenReturn(Optional.of(rating));

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attribute("rating", is(rating)));

        verify(ratingService, times(1)).getRatingById(1);
    }

    @Test
    void updateRating() throws Exception {
        Rating rating = new Rating("Moodys Rating 1", "Sand PRating 1", "Fitch Rating 1", 10);
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/rating/update/1")
                        .flashAttr("rating", rating)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        verify(ratingService, times(1)).updateRating(anyInt(), any(Rating.class));
    }


    @Test
    void deleteRating() throws Exception {
        doNothing().when(ratingService).deleteRating(1);

        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));

        verify(ratingService, times(1)).deleteRating(1);
    }
}
