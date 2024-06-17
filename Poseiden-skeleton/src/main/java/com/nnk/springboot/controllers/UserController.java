package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Displays the list of all Users.
     *
     * @param model the model to add attributes to
     * @return the view name for the User list
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user/list";
    }

    /**
     * Displays the form to add a new User.
     *
     * @param user the User to be added
     * @return the view name for adding a new User
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }

    /**
     * Validates and saves a new User to the database, then redirects to the User list.
     *
     * @param user the User to be validated and saved
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the User list if successful, otherwise the view name for adding a new User
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            if (!isValid(user.getPassword())) {
                result.rejectValue("password", "error.user", "Password must contain an uppercase letter, a digit, and a special character.");
                return "user/add";
            }
            userService.addUser(user);
            model.addAttribute("users", userService.getUsers());
            return "redirect:/user/list";

        }
        return "user/add";
    }

    /**
     * Displays the form to update an existing User.
     *
     * @param id the ID of the User to be updated
     * @param model the model to add attributes to
     * @return the view name for updating a User
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Validates and updates an existing User, then redirects to the User list.
     *
     * @param id the ID of the User to be updated
     * @param user the User to be updated
     * @param result the binding result for validation
     * @param model the model to add attributes to
     * @return the redirect URL to the User list if successful, otherwise the view name for updating a User
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }
        userService.updateUser(id, user);
        model.addAttribute("users", userService.getUsers());
        return "redirect:/user/list";
    }

    /**
     * Deletes an existing User and redirects to the User list.
     *
     * @param id the ID of the User to be deleted
     * @param model the model to add attributes to
     * @return the redirect URL to the User list
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.deleteUser(id);
        model.addAttribute("users", userService.getUsers());
        return "redirect:/user/list";
    }

    /**
     * Checks if the given password is valid.
     *
     * @param str the password to be validated
     * @return true if the password contains an uppercase letter, a digit, a special character, and is more than 8 characters long; false otherwise
     */
    public static boolean isValid(String str) {
        boolean hasUppercase = str.matches(".*[A-Z].*");
        boolean hasDigit = str.matches(".*[0-9].*");
        boolean hasSpecialChar = str.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        boolean hasMoreThan8Character = str.length() >= 8;

        return hasUppercase && hasDigit && hasSpecialChar && hasMoreThan8Character;
    }
}
