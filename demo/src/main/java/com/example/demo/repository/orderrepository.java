package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.orders;

public interface orderrepository extends CrudRepository<orders, String> {
    
}
