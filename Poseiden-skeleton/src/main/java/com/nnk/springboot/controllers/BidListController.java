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
     * Call service find all bids to show to the view
     * @param model
     * @return bid list
     */
    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.getAllBids());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "bidList/list";
    }

    /**
     *
     * @param bid
     * @return
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * Check data valid and save to db, after saving return bid list
     * @param bid
     * @param result
     * @param model
     * @return
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
     *
     * Get Bid by Id and to model then show to the form
     * @param id
     * @param model
     * @return bid list
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bid = bidListService.getBidById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id: " + id));
        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    /**
     * Check required fields, if valid call service to update Bid and return list Bid
     * @param id
     * @param bidList
     * @param result
     * @param model
     * @return
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
     * Find Bid by Id and delete the bid, return to Bid list
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.deleteBid(id);
        return "redirect:/bidList/list";
    }
}
