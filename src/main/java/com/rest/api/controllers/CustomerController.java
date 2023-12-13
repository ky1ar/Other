package com.rest.api.controllers;

import com.rest.api.exceptions.DataValidationException;
import com.rest.api.exceptions.UserConflictException;
import com.rest.api.models.Customer;
import com.rest.api.models.dto.ErrorReply;
import com.rest.api.models.request.RegistrationRequest;
import com.rest.api.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerNewCustomer(@RequestBody RegistrationRequest registrationRequest) {
        try {
            Customer registeredCustomer = userRegistrationService.processRegistration(registrationRequest);
            return new ResponseEntity<>(registeredCustomer , HttpStatus.CREATED);
        } catch (UserConflictException e) {
            return new ResponseEntity<>(new ErrorReply("Email already exists"), HttpStatus.CONFLICT);
        } catch (DataValidationException e) {
            return new ResponseEntity<>(new ErrorReply(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

