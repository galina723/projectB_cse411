package com.example.demo.controller.admin;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.*;

import jakarta.servlet.http.*;
@Controller
@RequestMapping("/admin")
public class ordercontroller {
    @Autowired
    private orderrepository orderrepo;
    @GetMapping("apps-ecommerce-order-details")
    public String orderdetail() {
        return ("admin/apps-ecommerce-order-details");
    }

    @GetMapping("apps-ecommerce-orders")
    public String orders(Model model) {
        List<orders> orders = (List<orders>) orderrepo.findAll();
        model.addAttribute("orders", orders);
        return ("admin/apps-ecommerce-orders");
    }
}
