package com.rest.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.exceptions.UserConflictException;
import com.rest.api.models.Customer;
import com.rest.api.models.Phone;
import com.rest.api.models.dto.ErrorReply;
import com.rest.api.models.request.RegistrationRequest;
import com.rest.api.service.UserRegistrationService;
import org.h2.engine.User;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRegistrationService userRegistrationService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController()).build();
    }

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        RegistrationRequest customerRequest = new RegistrationRequest();
        customerRequest.setFullName("John Doe");
        customerRequest.setEmailAddress("johndoe@example.com");
        customerRequest.setPassword("hunter2A2B#");

        Customer mockUser = new Customer("John Doe","johndoe@example.com","hunter2A2B#");

        when(userRegistrationService.processRegistration(customerRequest)).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = customerController.registerNewCustomer(customerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof User);
        verify(userRegistrationService, times(1)).processRegistration(customerRequest);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() throws Exception {
        // Arrange
        RegistrationRequest customerRequest = new RegistrationRequest();
        customerRequest.setFullName("Peter Jones");
        customerRequest.setEmailAddress("invalid@email.com");
        customerRequest.setPassword("password789ASV#");
        customerRequest.setPhoneNumbers(List.of(new Phone("123123","123","11"), new Phone("123123","23","55")));

        when(userRegistrationService.processRegistration(customerRequest)).thenThrow(new UserConflictException("El correo ya esta registrado"));

        // Act
        ResponseEntity<?> response = customerController.registerNewCustomer(customerRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorReply);
        assertEquals("Email already exists", ((ErrorReply) response.getBody()).getErrorMessage());
        verify(userRegistrationService, times(1)).processRegistration(customerRequest);
    }

    @Test
    public void testRegisterUser_InvalidData() {
        // Arrange
        RegistrationRequest userRequest = new RegistrationRequest();
        userRequest.setFullName("Peter Jones");
        userRequest.setEmailAddress("invalid@email");
        userRequest.setPassword("password789");

        when(userRegistrationService.processRegistration(userRequest)).thenThrow(new UserConflictException("Invalid email"));

        // Act
        ResponseEntity<?> response = customerController.registerNewCustomer(userRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorReply);
        assertEquals("Invalid email", ((ErrorReply) response.getBody()).getErrorMessage());
        verify(userRegistrationService, times(1)).processRegistration(userRequest);
    }
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String asJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
