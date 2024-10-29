package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.*;
import com.example.demo.otherfunction.encryption;

import org.springframework.ui.Model;
import com.example.demo.repository.customerrepository;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private customerrepository customerrepo;

    @GetMapping("index")
    public String index() {
        return "user/index";
    }

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("customers", new customers());
        return "user/register";
    }

    @PostMapping("/register/save")
    public String saveCustomer(@ModelAttribute customers customer) {
        // Mã hóa mật khẩu trước khi lưu
        String hashedPassword = encryption.encrypt(customer.getCustomerPassword());
        customer.setCustomerPassword(hashedPassword);
        customer.setCustomerStatus("active");

        customerrepo.save(customer);
        return "redirect:/user/login";
    }

    @GetMapping("login")
    public String login(Model model) {
        model.addAttribute("customers", new customers());
        return "user/login";
    }

    @PostMapping("/login/success")
    public String loginSubmit(@RequestParam("customerEmail") String email,
                              @RequestParam("CustomerPassword") String password,
                              Model model) {

        customers custo = customerrepo.findByCustomerEmail(email);
        if (custo == null) {
            return "redirect:/user/register";
        } else {
            if (encryption.encrypt(password).equals(custo.getCustomerPassword())) {
                return "redirect:/user/index";
            } else {
                model.addAttribute("loginError", "Invalid email or password");
                return "user/login";
            }
        }
    }

    @GetMapping("404")
    public String error() {
        return "user/404";
    }

    @GetMapping("about")
    public String about() {
        return "user/about";
    }

    @GetMapping("blog-details")
    public String blogdetail() {
        return "user/blog-details";
    }

    @GetMapping("blog")
    public String blog() {
        return "user/blog";
    }

    @GetMapping("cart")
    public String cart() {
        return "user/cart";
    }

    @GetMapping("checkout")
    public String checkout() {
        return "user/checkout";
    }

    @GetMapping("contact")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("faq")
    public String faq() {
        return "user/faq";
    }

    @GetMapping("forgot-password")
    public String forgotpassword() {
        return "user/forgot-password";
    }

    @GetMapping("my-account")
    public String myaccount() {
        return "user/my-account";
    }

    @GetMapping("product-details")
    public String productdetail() {
        return "user/product-details";
    }

    @GetMapping("shop")
    public String shop() {
        return "user/shop";
    }

    @GetMapping("wishlist")
    public String wishlist() {
        return "user/wishlist";
    }

}
