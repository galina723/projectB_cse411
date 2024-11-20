package com.example.demo.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.orders;
import com.example.demo.repository.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ordercontroller {

    @Autowired
    adminrepository adminrepo;

    @Autowired
    blogrepository blogrepo;

    @Autowired
    orderrepository orderrepo;

    // list orders

    @GetMapping("apps-ecommerce-order")
    public String order(Model model) {
        List<orders> orders = (List<orders>) orderrepo.findAll();
        model.addAttribute("orders", orders);
        return ("admin/apps-ecommerce-order");
    }

    @GetMapping("apps-ecommerce-order-detail/{id}")
    public String orderDetail(@PathVariable("id") int id, Model model) {
        orders order = orderrepo.findById(id).orElse(null);
        model.addAttribute("order", order);
        return ("admin/apps-ecommerce-order-detail");
    }
}