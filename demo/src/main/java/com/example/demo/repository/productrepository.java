package com.example.demo.repository;

import com.example.demo.model.products;

import org.springframework.data.repository.CrudRepository;

public interface productrepository extends CrudRepository<products, Integer> {
    
}
