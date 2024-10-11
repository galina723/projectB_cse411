package com.example.demo.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class test {

    @GetMapping("index.html")
    public String index() {
        return "user/index";
    }

    @GetMapping("404.html")
    public String error() {
        return "user/404";
    }
    
    @GetMapping("about.html")
    public String about() {
        return "user/about";
    }

    @GetMapping("blog-details.html")
    public String blogdetail() {
        return "user/blog-details";
    }

    @GetMapping("blog.html")
    public String blog() {
        return "user/blog";
    }

    @GetMapping("cart.html")
    public String cart() {
        return "user/cart";
    }

    @GetMapping("checkout.html")
    public String checkout() {
        return "user/checkout";
    }

    @GetMapping("contact.html")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("faq.html")
    public String faq() {
        return "user/faq";
    }

    @GetMapping("forgot-password.html")
    public String forgotpassword() {
        return "user/forgot-password";
    }

    @GetMapping("login.html")
    public String login() {
        return "user/login";
    }

    @GetMapping("my-account.html")
    public String myaccount() {
        return "user/my-account";
    }

    @GetMapping("product-details.html")
    public String productdetail() {
        return "user/product-details";
    }

    @GetMapping("shop.html")
    public String shop() {
        return "user/shop";
    }

    @GetMapping("wishlist.html")
    public String wishlist() {
        return "user/wishlist";
    }

}
