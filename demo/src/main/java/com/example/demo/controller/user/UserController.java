package com.example.demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("index")
    public String index() {
        return "user/index";
    }

    @GetMapping("register")
    public String register() {
        return "user/register";
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

    @GetMapping("login")
    public String login() {
        return "user/login";
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
