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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.admin;
import com.example.demo.model.blogs;
import com.example.demo.model.blogsdto;
import com.example.demo.model.customers;
import com.example.demo.otherfunction.encryption;
import com.example.demo.repository.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class admincontroller {

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

    @GetMapping("/auth-signin-basic")
    public String dashboard() {
        return "admin/auth-signin-basic";
    }

    @PostMapping("/login/success")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginSubmit(@RequestParam("AdminName") String name,
            @RequestParam("AdminPassword") String password, HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        admin admins = adminrepo.findByAdminName(name);

        if (admins == null) {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        session.setAttribute("loginAdmin", admins.getAdminId());
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("apps-ecommerce-sellers")
    public String sellers(Model model) {

        List<admin> admins = (List<admin>) adminrepo.findAll();
        model.addAttribute("admins", admins);
        return ("admin/apps-ecommerce-sellers");

    }

}