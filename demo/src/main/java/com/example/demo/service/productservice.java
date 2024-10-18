package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.products;
import com.example.demo.repository.productrepository;

@Service
public class productservice {

    @Autowired
    private productrepository productrepo;

    public List<products> listAll() {
        return (List<products>) productrepo.findAll();

    }
}
