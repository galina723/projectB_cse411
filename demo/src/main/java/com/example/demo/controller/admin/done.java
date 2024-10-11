package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class done {

    @GetMapping("index.html")
    public String index() {
        return "admin/index";
    }

    @GetMapping("layouts/index.html")
    public String index2() {
        return "admin/layouts/index";
    }

    @GetMapping("apps-chat.html")
    public String appchat() {
        return ("admin/apps-chat");
    }

    @GetMapping("apps-ecommerce-add-product.html")
    public String addproduct() {
        return ("admin/apps-ecommerce-add-product");
    }

    @GetMapping("apps-ecommerce-blog-details.html")
    public String blogdetail() {
        return ("admin/apps-ecommerce-blog-details");
    }

    @GetMapping("apps-ecommerce-blog.html")
    public String blog() {
        return ("admin/apps-ecommerce-blog");
    }

    @GetMapping("apps-ecommerce-create-blog.html")
    public String createblog() {
        return ("admin/apps-ecommerce-create-blog");
    }

    @GetMapping("apps-ecommerce-customers.html")
    public String customers() {
        return ("admin/apps-ecommerce-customers");
    }

    @GetMapping("apps-ecommerce-order-details.html")
    public String orderdetail() {
        return ("admin/apps-ecommerce-order-details");
    }

    @GetMapping("apps-ecommerce-orders.html")
    public String orders() {
        return ("admin/apps-ecommerce-orders");
    }

    @GetMapping("apps-ecommerce-product-details.html")
    public String productdetail() {
        return ("admin/apps-ecommerce-product-details");
    }

    @GetMapping("apps-ecommerce-products.html")
    public String product() {
        return ("admin/apps-ecommerce-products");
    }

    @GetMapping("apps-ecommerce-seller-details.html")
    public String sellerdetail() {
        return ("admin/apps-ecommerce-seller-details");
    }

    @GetMapping("apps-ecommerce-sellers.html")
    public String sellers() {
        return ("admin/apps-ecommerce-sellers");
    }

    @GetMapping("apps-invoices-details.html")
    public String invoicedetail() {
        return ("admin/apps-invoices-details");
    }

    @GetMapping("pages-account-settings.html")
    public String setting() {
        return ("admin/pages-account-settings");
    }

}
