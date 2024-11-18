package com.example.demo.controller.admin;

import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.sql.Date;
import java.util.List;

import org.hibernate.engine.jdbc.mutation.spi.BindingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.*;
import com.example.demo.repository.adminrepository;
import com.example.demo.repository.blogrepository;
import com.example.demo.repository.customerrepository;
import com.example.demo.repository.orderrepository;
import com.example.demo.repository.productrepository;

import jakarta.persistence.criteria.CriteriaBuilder.In;

import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("/deleteCustomer/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            customerrepo.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Customer not found or already deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the customer.");
        }

        return "redirect:/admin/apps-ecommerce-customers";
    }

    // @PostMapping("/editCustomer")
    // public String saveEditedCategory(@ModelAttribute("customersdto") customersdto
    // customersdto, BindingResult result) {
    // if (result.hasErrors()) {
    // return "admin/apps-ecommerce-customers"; // Return the same page if errors
    // are found
    // }

    // Integer customerId = customersdto.getCustomerId();
    // customers existingCustomer = customerrepo.findById(customerId).orElse(null);
    // if (existingCustomer == null) {
    // result.addError(new FieldError("customersdto", "CustomerId", "Customer not
    // found!"));
    // return "admin/apps-ecommerce-customers"; // Return with error if customer not
    // found
    // }

    // // Update the customer status
    // existingCustomer.setCustomerStatus(customersdto.getCustomerStatus());
    // existingCustomer.setCustomerName(customersdto.getCustomerName());

    // customerrepo.save(existingCustomer);

    // return "redirect:/admin/apps-ecommerce-customers"; // Redirect after saving
    // }

    @GetMapping("apps-ecommerce-seller-details")
    public String sellerdetail() {
        return ("admin/apps-ecommerce-seller-details");
    }

    @GetMapping("apps-ecommerce-sellers")
    public String sellers(Model model) {

        List<admin> admins = (List<admin>) adminrepo.findAll();
        model.addAttribute("admins", admins);
        return ("admin/apps-ecommerce-sellers");
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