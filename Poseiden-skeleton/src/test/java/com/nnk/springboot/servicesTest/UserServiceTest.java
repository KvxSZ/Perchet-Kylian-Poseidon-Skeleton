package com.nnk.springboot.servicesTest;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers() {
        User user1 = new User();
        user1.setUsername("User1");
        User user2 = new User();
        user2.setUsername("User2");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setUsername("User1");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals("User1", result.get().getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void addUser() {
        User user = new User("username", "password", "USER");

        userService.addUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("User1");
        existingUser.setPassword("oldPassword");
        User updatedUser = new User();
        updatedUser.setUsername("UpdatedUser");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));

        userService.updateUser(1, updatedUser);
        existingUser.setPassword("encodedPassword");

        verify(userRepository, times(1)).save(existingUser);
        assertEquals("UpdatedUser", existingUser.getUsername());
        assertEquals("encodedPassword", existingUser.getPassword());
    }

    @Test
    void deleteUser() {
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void updateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setUsername("UpdatedUser");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1, updatedUser);
        });

        String expectedMessage = "Invalid user Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(1);
        });

        String expectedMessage = "Invalid user Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
