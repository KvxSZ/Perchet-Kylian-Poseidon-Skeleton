package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;

    /**
     * Displays the list of all Ratings.
     *
     * @param model the model to add attributes to
     * @return the view name for the rating list
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.getRatings());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "rating/list";
    }

    /**
     * Displays the form to add a new Rating.
     *
     * @param rating the rating to be added
     * @return the view name for adding a new rating
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    /**
     * Validates and saves a new Rating to the database, then redirects to the Rating list.
     *
     * @param rating the rating to be validated and saved
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the rating list if successful, otherwise the view name for adding a new rating
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ratingService.addRating(rating);
            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    /**
     * Displays the form to update an existing Rating.
     *
     * @param id the ID of the rating to be updated
     * @param model the model to add attributes to
     * @return the view name for updating a rating
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.getRatingById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id: " + id));
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    /**
     * Validates and updates an existing Rating, then redirects to the Rating list.
     *
     * @param id the ID of the rating to be updated
     * @param rating the rating to be updated
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the rating list if successful, otherwise the view name for updating a rating
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rating", rating);
            return "rating/update";
        }
        ratingService.updateRating(id, rating);
        return "redirect:/rating/list";
    }

    /**
     * Deletes an existing Rating and redirects to the Rating list.
     *
     * @param id the ID of the rating to be deleted
     * @param model the model to add attributes to
     * @return the redirect URL to the rating list
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteRating(id);
        return "redirect:/rating/list";
    }
}
