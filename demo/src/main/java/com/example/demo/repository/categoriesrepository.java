package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.categories;

public interface categoriesrepository extends CrudRepository<categories, Integer> {

}
