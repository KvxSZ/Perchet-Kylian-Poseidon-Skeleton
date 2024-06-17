package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurveService;
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
public class CurveController {

    @Autowired
    private CurveService curveService;

    /**
     * Displays the list of all CurvePoints.
     *
     * @param model the model to add attributes to
     * @return the view name for the curve point list
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curveService.getAllCurvePoints());
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "curvePoint/list";
    }

    /**
     * Displays the form to add a new CurvePoint.
     *
     * @param curvePoint the curve point to be added
     * @return the view name for adding a new curve point
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {
        return "curvePoint/add";
    }

    /**
     * Validates and saves a new CurvePoint to the database, then redirects to the CurvePoint list.
     *
     * @param curvePoint the curve point to be validated and saved
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the curve point list if successful, otherwise the view name for adding a new curve point
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            curveService.addCurvePoint(curvePoint);
            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    /**
     * Displays the form to update an existing CurvePoint.
     *
     * @param id the ID of the curve point to be updated
     * @param model the model to add attributes to
     * @return the view name for updating a curve point
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curveService.getCurvePointById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint Id: " + id));
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    /**
     * Validates and updates an existing CurvePoint, then redirects to the CurvePoint list.
     *
     * @param id the ID of the curve point to be updated
     * @param curvePoint the curve point to be updated
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the curve point list if successful, otherwise the view name for updating a curve point
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        }
        curveService.updateCurvePoint(id, curvePoint);
        return "redirect:/curvePoint/list";
    }

    /**
     * Deletes an existing CurvePoint and redirects to the CurvePoint list.
     *
     * @param id the ID of the curve point to be deleted
     * @param model the model to add attributes to
     * @return the redirect URL to the curve point list
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        curveService.deleteCurvePoint(id);
        return "redirect:/curvePoint/list";
    }
}