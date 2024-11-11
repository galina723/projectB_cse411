package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.*;

public interface cartrepository extends CrudRepository<carts, CartId> {

    @Query("SELECT c FROM carts c WHERE c.id.customerId = :customerId")
    List<carts> findByCustomerId(@Param("customerId") Integer customerId);


}
