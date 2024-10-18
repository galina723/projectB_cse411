package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.customers;
import com.example.demo.repository.customerrepository;

import java.util.*;

@Service
public class customer {

    @Autowired private customerrepository customerrepo;

    public List<customers> listAll() {

        return (List<customers>) customerrepo.findAll();

    }

}
