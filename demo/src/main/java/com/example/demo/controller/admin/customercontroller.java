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

    // @GetMapping("apps-ecommerce-blog-details")
    // public String blogdetail() {
    // return ("admin/apps-ecommerce-blog-details");
    // }

    // @GetMapping("apps-ecommerce-blog")
    // public String blog(Model model) {

    // List<blogs> blogs = (List<blogs>) blogrepo.findAll();
    // model.addAttribute("blogs", blogs);
    // return ("admin/apps-ecommerce-blog");
    // }

    // @GetMapping("apps-ecommerce-create-blog")
    // public String createblog(Model model) {
    // productsdto productsdto = new productsdto();
    // model.addAttribute("productsdto", productsdto);
    // return ("admin/apps-ecommerce-create-blog");
    // }

    // @GetMapping("apps-ecommerce-edit-blog")
    // public String editblog() {
    // return ("admin/apps-ecommerce-edit-blog");
    // }

    @GetMapping("apps-ecommerce-customers")
    public String customers(Model model) {

        List<customers> customers = (List<customers>) customerrepo.findAll();
        model.addAttribute("customers", customers);
        return ("admin/apps-ecommerce-customers");
    }
    

    @GetMapping("apps-ecommerce-order-details")
    public String orderdetail() {
        return ("admin/apps-ecommerce-order-details");
    }

    @GetMapping("apps-ecommerce-orders")
    public String orders() {
        return ("admin/apps-ecommerce-orders");
    }

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
