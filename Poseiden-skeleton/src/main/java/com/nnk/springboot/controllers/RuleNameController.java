package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
public class RuleNameController {
    @Autowired
    RuleNameService ruleNameService;

    /**
     * Displays the list of all RuleNames.
     *
     * @param model the model to add attributes to
     * @return the view name for the RuleName list
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        model.addAttribute("ruleNames", ruleNameService.getRuleNames());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "ruleName/list";
    }

    /**
     * Displays the form to add a new RuleName.
     *
     * @param ruleName the RuleName to be added
     * @return the view name for adding a new RuleName
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName) {
        return "ruleName/add";
    }

    /**
     * Validates and saves a new RuleName to the database, then redirects to the RuleName list.
     *
     * @param ruleName the RuleName to be validated and saved
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the RuleName list if successful, otherwise the view name for adding a new RuleName
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ruleNameService.addRuleName(ruleName);
            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    /**
     * Displays the form to update an existing RuleName.
     *
     * @param id the ID of the RuleName to be updated
     * @param model the model to add attributes to
     * @return the view name for updating a RuleName
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameService.getRuleNameById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id: " + id));
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    /**
     * Validates and updates an existing RuleName, then redirects to the RuleName list.
     *
     * @param id the ID of the RuleName to be updated
     * @param ruleName the RuleName to be updated
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the RuleName list if successful, otherwise the view name for updating a RuleName
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        }
        ruleNameService.updateRuleName(id, ruleName);
        return "redirect:/ruleName/list";
    }

    /**
     * Deletes an existing RuleName and redirects to the RuleName list.
     *
     * @param id the ID of the RuleName to be deleted
     * @param model the model to add attributes to
     * @return the redirect URL to the RuleName list
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleNameService.deleteRuleName(id);
        return "redirect:/ruleName/list";
    }
}
