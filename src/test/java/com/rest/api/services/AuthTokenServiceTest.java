package com.rest.api.services;

import com.rest.api.models.Customer;
import com.rest.api.service.AuthTokenService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTokenServiceTest {

    @Autowired
    private AuthTokenService authTokenService;

    @BeforeEach
    public void setup() {
        // Set values for the @Value fields using ReflectionTestUtils
        ReflectionTestUtils.setField(authTokenService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(authTokenService, "jwtExpiration", 86400000); // Example expiration time in milliseconds
    }

    @Test
    public void testGenerateAndExtractToken() {
        // Create a sample user
        Customer customer = new Customer();
        customer.setEmailAddress("test@example.com");

        // Generate a token
        String token = authTokenService.generateToken(customer);

        // Extract claim and verify
        String email = authTokenService.extractClaim(token, Claims::getSubject);
        assertEquals("test@example.com", email);
    }

}