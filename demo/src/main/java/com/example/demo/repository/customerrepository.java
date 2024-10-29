package com.example.demo.repository;

import com.example.demo.model.customers;

import org.springframework.data.repository.CrudRepository;

public interface customerrepository extends CrudRepository<customers, Integer> {
    customers findByCustomerEmail(String customerEmail);

}
