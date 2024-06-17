package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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
public class BidListController {

    @Autowired
    private BidListService bidListService;

    /**
     * Displays the list of all bids.
     *
     * @param model the model to add attributes to
     * @return the view name for the bid list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListService.getAllBids());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "bidList/list";
    }

    /**
     * Displays the form to add a new bid.
     *
     * @param bid the bid to be added
     * @return the view name for adding a new bid
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * Validates and saves a new bid to the database, then redirects to the bid list.
     *
     * @param bid the bid to be validated and saved
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the bid list if successful, otherwise the view name for adding a new bid
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            bidListService.addBid(bid);
            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    /**
     * Displays the form to update an existing bid.
     *
     * @param id the ID of the bid to be updated
     * @param model the model to add attributes to
     * @return the view name for updating a bid
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bid = bidListService.getBidById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id: " + id));
        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    /**
     * Validates and updates an existing bid, then redirects to the bid list.
     *
     * @param id the ID of the bid to be updated
     * @param bidList the bid to be updated
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the bid list if successful, otherwise the view name for updating a bid
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        }
        bidListService.updateBid(id, bidList);
        return "redirect:/bidList/list";
    }

    /**
     * Deletes an existing bid and redirects to the bid list.
     *
     * @param id the ID of the bid to be deleted
     * @param model the model to add attributes to
     * @return the redirect URL to the bid list
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.deleteBid(id);
        return "redirect:/bidList/list";
    }
}