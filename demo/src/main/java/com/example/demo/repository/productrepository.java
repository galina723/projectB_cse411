package com.example.demo.repository;

import com.example.demo.model.Products;

import org.springframework.data.jpa.repository.JpaRepository;

public interface productrepository extends JpaRepository<Products, String> {
    
}
