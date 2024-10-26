package com.example.demo.repository;

import com.example.demo.model.products;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface productrepository extends CrudRepository<products, Integer> {
    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM product", nativeQuery = true)
    int findNextProductId();
}
