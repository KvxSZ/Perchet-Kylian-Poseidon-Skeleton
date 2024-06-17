package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {
    @Autowired
    TradeService tradeService;

    /**
     * Displays the list of all Trades.
     *
     * @param model the model to add attributes to
     * @return the view name for the Trade list
     */
    @RequestMapping("/trade/list")
    public String home(Model model) {
        model.addAttribute("trades", tradeService.getTrades());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "trade/list";
    }

    /**
     * Displays the form to add a new Trade.
     *
     * @param trade the Trade to be added
     * @return the view name for adding a new Trade
     */
    @GetMapping("/trade/add")
    public String addUser(Trade trade) {
        return "trade/add";
    }

    /**
     * Validates and saves a new Trade to the database, then redirects to the Trade list.
     *
     * @param trade the Trade to be validated and saved
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the Trade list if successful, otherwise the view name for adding a new Trade
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            tradeService.addTrade(trade);
            return "redirect:/trade/list";
        }
        return "trade/add";
    }

    /**
     * Displays the form to update an existing Trade.
     *
     * @param id the ID of the Trade to be updated
     * @param model the model to add attributes to
     * @return the view name for updating a Trade
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.getTradeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id: " + id));
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    /**
     * Validates and updates an existing Trade, then redirects to the Trade list.
     *
     * @param id the ID of the Trade to be updated
     * @param trade the Trade to be updated
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the Trade list if successful, otherwise the view name for updating a Trade
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trade", trade);
            return "trade/update";
        }
        tradeService.updateTrade(id, trade);
        return "redirect:/trade/list";
    }

    /**
     * Deletes an existing Trade and redirects to the Trade list.
     *
     * @param id the ID of the Trade to be deleted
     * @param model the model to add attributes to
     * @return the redirect URL to the Trade list
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(id);
        return "redirect:/trade/list";
    }
}
