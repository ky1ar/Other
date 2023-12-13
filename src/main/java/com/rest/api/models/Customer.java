package com.rest.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(
    name = "customers",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = "emailAddress")
    })
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerId;

    @JsonIgnore
    private String fullName;

    @JsonIgnore
    private String emailAddress;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String authToken;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Phone> phoneNumbers;

    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;

    @Column(name = "lastLoginDate")
    private LocalDateTime lastLoginDate;

    @Column(name = "isActive")
    private Boolean isActive;

    public Customer(String fullName, String emailAddress, String password) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.password = password;
    }


}