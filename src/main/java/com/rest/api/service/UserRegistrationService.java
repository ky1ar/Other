package com.rest.api.service;

import com.rest.api.models.Customer;
import com.rest.api.models.request.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserRegistrationService {
    Customer processRegistration(RegistrationRequest userRequest);
}
