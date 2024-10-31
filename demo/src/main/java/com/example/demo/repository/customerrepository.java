package com.example.demo.repository;

import com.example.demo.model.customers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface customerrepository extends CrudRepository<customers, Integer> {
    customers findByCemail(String cemail);
    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM product", nativeQuery = true)
    int findNextCustomerId();
}
