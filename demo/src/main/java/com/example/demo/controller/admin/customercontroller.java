package com.example.demo.controller.admin;

import java.util.List;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.*;
import com.example.demo.repository.adminrepository;
import com.example.demo.repository.blogrepository;
import com.example.demo.repository.customerrepository;
import com.example.demo.repository.orderrepository;
import com.example.demo.repository.productrepository;

@Controller
@RequestMapping("/admin")
public class customercontroller {

    @Autowired
    customerrepository customerrepo;

    @Autowired
    productrepository productrepo;

    @Autowired
    orderrepository orderrepo;

    @Autowired
    adminrepository adminrepo;

    @Autowired
    blogrepository blogrepo;

    @GetMapping("index")
    public String dashboard() {
        return ("admin/index");
    }

    @GetMapping("apps-ecommerce-customers")
    public String customers(Model model) {
        List<customers> customers = (List<customers>) customerrepo.findAll();
        model.addAttribute("customers", customers);
        return ("admin/apps-ecommerce-customers");
    }

    @PostMapping("/editCustomer")
    public String editCustomer(@ModelAttribute("customersdto") customersdto customersdto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-customers";
        }
        Integer customerId = customersdto.getCustomerId();
        customers existingCustomer = customerrepo.findById(customerId).orElse(null);
        if (existingCustomer == null) {
            result.addError(new FieldError("customersdto", "CustomerId", "Customer not found!"));
            return "admin/apps-ecommerce-customers";
        }
        existingCustomer.setCustomerStatus(customersdto.getCustomerStatus());
        customerrepo.save(existingCustomer);

        return "redirect:/admin/apps-ecommerce-customers";
    }

    @GetMapping("apps-ecommerce-seller-details")
    public String sellerdetail() {
        return ("admin/apps-ecommerce-seller-details");
    }

    

    @GetMapping("apps-invoices-details")
    public String invoicedetail() {
        return ("admin/apps-invoices-details");
    }

    @GetMapping("pages-profile-settings")
    public String setting() {
        return ("admin/pages-profile-settings");
    }

}