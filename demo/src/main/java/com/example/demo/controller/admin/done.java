package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class done {

    @GetMapping("index")
    public String dashboard() {
        return ("admin/index");
    }

    @GetMapping("apps-ecommerce-add-product")
    public String addproduct() {
        return ("admin/apps-ecommerce-add-product");
    }

    @GetMapping("apps-ecommerce-blog-details")
    public String blogdetail() {
        return ("admin/apps-ecommerce-blog-details");
    }

    @GetMapping("apps-ecommerce-blog")
    public String blog() {
        return ("admin/apps-ecommerce-blog");
    }

    @GetMapping("apps-ecommerce-create-blog")
    public String createblog() {
        return ("admin/apps-ecommerce-create-blog");
    }

    @GetMapping("apps-ecommerce-customers")
    public String customers() {
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

    @GetMapping("apps-ecommerce-product-details")
    public String productdetail() {
        return ("admin/apps-ecommerce-product-details");
    }

    @GetMapping("apps-ecommerce-products")
    public String product() {
        return ("admin/apps-ecommerce-products");
    }

    @GetMapping("apps-ecommerce-seller-details")
    public String sellerdetail() {
        return ("admin/apps-ecommerce-seller-details");
    }

    @GetMapping("apps-ecommerce-sellers")
    public String sellers() {
        return ("admin/apps-ecommerce-sellers");
    }

    @GetMapping("apps-invoices-details")
    public String invoicedetail() {
        return ("admin/apps-invoices-details");
    }

    @GetMapping("pages-account-settings")
    public String setting() {
        return ("admin/pages-account-settings");
    }

}
