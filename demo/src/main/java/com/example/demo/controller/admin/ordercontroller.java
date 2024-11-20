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
import com.example.demo.repository.*;

@Controller
@RequestMapping("admin")
public class ordercontroller {

    @Autowired
    orderrepository orderrepo;

    @GetMapping("apps-ecommerce-order-details")
    public String orderdetail(Model model) {

        return ("admin/apps-ecommerce-order-details");
    }

    @GetMapping("apps-ecommerce-orders")
    public String orders(Model model) {
        List<orders> order = (List<orders>) orderrepo.findAll();
        model.addAttribute("orders", order);
        return ("admin/apps-ecommerce-orders");
    }

    @PostMapping("/editOrder")
    public String saveEditedCategory(@ModelAttribute("ordersdto") ordersdto ordersdto,
            BindingResult result) {
        if (result.hasErrors()) {
            return "admin/apps-ecommerce-orders";
        }

        Integer orderId = ordersdto.getOrderId();
        orders existingOrder = orderrepo.findById(orderId).orElse(null);
        if (existingOrder == null) {
            result.addError(new FieldError("ordersdto", "OrderId", "Order not found!"));
            return "admin/apps-ecommerce-orders";
        }

        existingOrder.setOrderStatus(ordersdto.getOrderStatus());

        orderrepo.save(existingOrder);


        return "redirect:/admin/apps-ecommerce-orders";
    }

}
