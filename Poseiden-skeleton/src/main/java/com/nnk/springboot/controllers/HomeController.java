package com.nnk.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

	/**
	 * Displays the home page.
	 *
	 * @param model the model to add attributes to
	 * @return the view name for the home page
	 */
	@RequestMapping("/")
	public String home(Model model) {
		return "home";
	}

	/**
	 * Redirects to the bid list page for admin users.
	 *
	 * @param model the model to add attributes to
	 * @return the redirect URL to the bid list page
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model) {
		return "redirect:/bidList/list";
	}
}
