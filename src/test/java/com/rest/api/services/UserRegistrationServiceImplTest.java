package com.rest.api.services;

import com.rest.api.exceptions.UserConflictException;
import com.rest.api.models.Customer;
import com.rest.api.models.request.RegistrationRequest;
import com.rest.api.repositories.CustomerRepository;
import com.rest.api.service.impl.UserRegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserRegistrationServiceImplTest {

    @Mock
    private CustomerRepository userRepository;

    @InjectMocks
    private UserRegistrationServiceImpl userService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(userService, "passwordRegex", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z])[a-zA-Z\\\\d@#$%^&+=!]{8,}$");
    }

    @Test
    public void testRegisterUser_InvalidEmailFormat() {
        // Arrange
        RegistrationRequest userRequest = new RegistrationRequest();
        userRequest.setEmailAddress("invalidEmail");
        userRequest.setPassword("hunter2A2B#");
        userRequest.setFullName("John Doe");

        // Act and Assert
        assertThrows(UserConflictException.class, () -> userService.processRegistration(userRequest));
        verify(userRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testRegisterUser_InvalidPasswordFormat() {
        // Arrange
        RegistrationRequest userRequest = new RegistrationRequest();
        userRequest.setEmailAddress("test@example.com");
        userRequest.setPassword("invalidPassword");
        userRequest.setFullName("John Doe");

        // Act and Assert
        assertThrows(UserConflictException.class, () -> userService.processRegistration(userRequest));
        verify(userRepository, never()).save(any(Customer.class));
    }
}