package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.orders;

public interface orderrepository extends CrudRepository<orders, Integer> {
    @Query("SELECT o FROM orders o WHERE o.CustomerId = :customerId")
    List<orders> findOrdersByCustomerId(@Param("customerId") int customerId);
}
