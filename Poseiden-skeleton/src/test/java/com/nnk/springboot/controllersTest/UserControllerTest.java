package com.nnk.springboot.controllersTest;


import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setPassword("Test123!1516");
    }

    @Test
    void testHome() {
        when(userService.getUsers()).thenReturn(Arrays.asList(user));
        String view = userController.home(model);
        assertEquals("user/list", view);
        verify(model, times(1)).addAttribute("users", userService.getUsers());
    }

    @Test
    void testAddUser() {
        String view = userController.addUser(new User());
        assertEquals("user/add", view);
    }

    @Test
    void testValidate_Success() {
        when(result.hasErrors()).thenReturn(false);
        when(userService.getUsers()).thenReturn(Collections.singletonList(user));

        String view = userController.validate(user, result, model);

        assertEquals("redirect:/user/list", view);
        verify(userService, times(1)).addUser(any(User.class));
        verify(model, times(1)).addAttribute("users", userService.getUsers());
    }

    @Test
    void testValidate_HasErrors() {
        when(result.hasErrors()).thenReturn(true);

        String view = userController.validate(user, result, model);

        assertEquals("user/add", view);
        verify(userService, never()).addUser(any(User.class));
    }

    @Test
    void testValidate_InvalidPassword() {
        when(result.hasErrors()).thenReturn(false);

        user.setPassword("invalid");

        String view = userController.validate(user, result, model);

        assertEquals("user/add", view);
        verify(userService, never()).addUser(any(User.class));
        verify(result).rejectValue("password", "error.user", "Password must contain an uppercase letter, a digit, and a special character.");
    }

    @Test
    void testShowUpdateForm() {
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(user));

        String view = userController.showUpdateForm(1, model);

        assertEquals("user/update", view);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    void testUpdateUser_Success() {
        when(result.hasErrors()).thenReturn(false);
        when(userService.getUsers()).thenReturn(Arrays.asList(user));

        String view = userController.updateUser(1, user, result, model);

        assertEquals("redirect:/user/list", view);
        verify(userService, times(1)).updateUser(anyInt(), any(User.class));
        verify(model, times(1)).addAttribute("users", userService.getUsers());
    }

    @Test
    void testUpdateUser_HasErrors() {
        when(result.hasErrors()).thenReturn(true);

        String view = userController.updateUser(1, user, result, model);

        assertEquals("user/update", view);
        verify(userService, never()).updateUser(anyInt(), any(User.class));
    }

    @Test
    void testDeleteUser() {
        when(userService.getUsers()).thenReturn(Arrays.asList(user));

        String view = userController.deleteUser(1, model);

        assertEquals("redirect:/user/list", view);
        verify(userService, times(1)).deleteUser(anyInt());
        verify(model, times(1)).addAttribute("users", userService.getUsers());
    }

    @Test
    void testIsValid_Password() {
        assertEquals(true, UserController.isValid("Valid1!password"));
        assertEquals(false, UserController.isValid("invalid"));
        assertEquals(false, UserController.isValid("Invalid1"));
        assertEquals(false, UserController.isValid("invalid!password"));
        assertEquals(false, UserController.isValid("VALID1!"));
    }
}
