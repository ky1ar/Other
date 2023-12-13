package com.rest.api.repositories;

import com.rest.api.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findById(UUID id);

    Customer findByEmailAddress(String emailAddress);

    Customer save(Customer customer);
}
