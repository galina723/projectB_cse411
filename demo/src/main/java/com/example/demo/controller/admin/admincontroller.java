package com.example.demo.controller.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.admin;
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
        if (adminrepo.existsByEmail("yenhopie28@gmail.com") == false) {
            admin admin = new admin();
            admin.setAdminName("yenhopie28");
            admin.setAdminEmail("yenhopie28@gmail.com");
            String hashPassword = encryption.encrypt("yenhopie28");
            admin.setAdminPassword(hashPassword);
            admin.setAdminPhone("0123456789");
            admin.setAdminStatus("Active");
            adminrepo.save(admin);
        }
        return "admin/auth-signin-basic";
    }

    @PostMapping("/login/success")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> loginSubmit(@RequestParam("AdminEmail") String email,
            @RequestParam("AdminPassword") String password, HttpSession session) {

        Map<String, Object> response = new HashMap<>();
        admin ad = adminrepo.findByAdminEmail(email);

        if (ad != null) {
            if (ad.getAdminEmail().equals("yenhopie28@gmail.com")
                    && encryption.matches(password, ad.getAdminPassword())) {
                session.setAttribute("loginAdmin", ad.getAdminId());
                response.put("success", true);
                return ResponseEntity.ok(response);
            } else if (encryption.matches(password, ad.getAdminPassword())) {
                session.setAttribute("loginAdmin2", ad.getAdminId());
                response.put("success", true);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @GetMapping("apps-ecommerce-sellers")
    public String sellers(Model model) {

        List<admin> admins = (List<admin>) adminrepo.findAll();
        model.addAttribute("admins", admins);
        return ("admin/apps-ecommerce-sellers");
    }

}