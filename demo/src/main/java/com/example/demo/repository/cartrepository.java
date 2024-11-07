package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.carts;

public interface cartrepository extends CrudRepository<carts, Integer> {
    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM cart", nativeQuery = true)
    int findNextCartId();

    @Query("SELECT c FROM carts c WHERE c.customerId = :customerId")
    List<carts> findByCustomerId(@Param("customerId") Integer customerId);
}
