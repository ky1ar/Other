package com.rest.api.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "customer_phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID phoneId;

    private String number;
    private String citycode;
    private String countrycode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Phone(String number, String citycode, String countrycode) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
    }
}
