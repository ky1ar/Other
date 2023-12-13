package com.rest.api.models.request;

import com.rest.api.models.Phone;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistrationRequest {
    private String fullName;
    private String emailAddress;
    private String password;
    private List<Phone> phoneNumbers;
}
