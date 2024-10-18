package com.example.demo.repository;

import com.example.demo.model.customers;
import com.example.demo.model.products;

import org.springframework.data.repository.CrudRepository;

public interface customerrepository extends CrudRepository<customers, String> {

}
