package com.example.demo.controller.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.otherfunction.encryption;
import com.example.demo.repository.*;
import org.springframework.data.domain.Pageable;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class indexcontroller {

    @Autowired
    adminrepository adminrepo;

    @Autowired
    blogrepository blogrepo;

    @Autowired
    productrepository productrepo;

    @Autowired
    orderrepository orderrepo;

    @Autowired
    customerrepository customerrepo;

    @Autowired
    categoriesrepository caterepo;

    @Autowired
    productimagesrepository productimagerepo;

    @Autowired
    cartrepository cartrepo;

    @Autowired
    orderdetailrepository orderdetailsrepo;

    @GetMapping("index")
    public String dashboard(Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        Integer adminId = (Integer) session.getAttribute("loginAdmin");
        Integer superId = (Integer) session.getAttribute("loginSuper");

        if (adminId == null && superId == null) {
            redirectAttributes.addFlashAttribute("loginRequired", "Please log in to view this page.");
            return "redirect:/admin/auth-signin-basic";
        }
        Long totalOrderAmount = orderrepo.findTotalOrderAmount();
        model.addAttribute("totalOrderAmount", totalOrderAmount);

        Long totalOrders = orderrepo.countAllOrders();
        model.addAttribute("totalOrders", totalOrders);

        Long totalCustomers = customerrepo.countAllCustomers();
        model.addAttribute("totalCustomers", totalCustomers);

        Pageable pageable = PageRequest.of(0, 5);
        List<products> product = productrepo.findTop5Products(pageable);
        model.addAttribute("products", product);

        Pageable pageable1 = PageRequest.of(0, 5);
        List<customers> customer = customerrepo.findTop5Customers(pageable1);
        model.addAttribute("customers", customer);

        Pageable pageable2 = PageRequest.of(0, 5);
        List<orders> order = orderrepo.findTop50rders(pageable2);
        model.addAttribute("orders", order);

        return ("admin/index");
    }

}
