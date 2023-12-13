package com.rest.api.service.impl;

import com.rest.api.exceptions.DataValidationException;
import com.rest.api.exceptions.UserConflictException;
import com.rest.api.models.Phone;
import com.rest.api.models.Customer;
import com.rest.api.models.request.RegistrationRequest;
import com.rest.api.repositories.CustomerRepository;
import com.rest.api.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.rest.api.service.AuthTokenService AuthTokenService;

    @Value("${password.regex}")
    private String passwordRegex;

    @Override
    public Customer processRegistration(RegistrationRequest registrationRequest) {

        if (!isValidEmailFormat(registrationRequest.getEmailAddress())) {
            throw new DataValidationException("Invalid email format");
        }

        if (!isValidPasswordFormat(registrationRequest.getPassword())) {
            throw new DataValidationException("Invalid password format");
        }

        if (customerRepository.findByEmailAddress(registrationRequest.getEmailAddress()) != null) {
            throw new UserConflictException(registrationRequest.getEmailAddress());
        }

        Customer newCustomer = new Customer();
        newCustomer.setFullName(registrationRequest.getFullName());
        newCustomer.setEmailAddress(registrationRequest.getEmailAddress());
        newCustomer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

        List<Phone> phoneNumbers = registrationRequest.getPhoneNumbers();
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            newCustomer.setPhoneNumbers(phoneNumbers);
            for (Phone phone : phoneNumbers) {
                phone.setCustomer(newCustomer);
            }
        }

        newCustomer.setCreatedDate(LocalDateTime.now());
        newCustomer.setLastModifiedDate(LocalDateTime.now());
        newCustomer.setLastLoginDate(newCustomer.getCreatedDate());
        newCustomer.setAuthToken(AuthTokenService.generateToken(newCustomer));
        newCustomer.setIsActive(true);

        Customer customer = customerRepository.save(newCustomer);

        return customer;
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    public boolean isValidPasswordFormat(String password) {
        Pattern pattern = Pattern.compile(passwordRegex);
        return password != null && pattern.matcher(password).matches();
    }
}