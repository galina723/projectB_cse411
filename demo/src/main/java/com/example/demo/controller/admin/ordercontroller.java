package com.example.demo.controller.admin;

import java.util.ArrayList;
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

    @Autowired
    orderdetailrepository orderdetailsrepo;

    @Autowired
    productrepository productrepo;

    @Autowired
    customerrepository customerrepo;

    @GetMapping("apps-ecommerce-order-details/{id}")
    public String orderdetail(@PathVariable("id") int id, Model model) {
        orders order = orderrepo.findById(id).orElse(null);
        customers customer = customerrepo.findById(order.getCustomer().getCustomerId()).orElse(null);
        
        List<orderdetails> orderDetailsList = orderdetailsrepo.findByOrderId(id);
        List<orderdetailsdto> orderDetailsDTOs = new ArrayList<>();
        for (orderdetails orderDetail : orderDetailsList) {
            products product = productrepo.findById(orderDetail.getProductId()).orElse(null);
            if (product != null) {
                orderdetailsdto dto = new orderdetailsdto();
                dto.setProductId(product.getProductId());
                dto.setProductName(product.getProductName());
                dto.setProductMainImage(product.getProductMainImage());
                dto.setQuantity(orderDetail.getProductQuantity());
                dto.setProductPrice(product.getProductPrice());
                dto.setTotalPrice(orderDetail.getProductPrice() * orderDetail.getProductQuantity());
                orderDetailsDTOs.add(dto);
            }
        }

        model.addAttribute("customers", customer);
        model.addAttribute("orders", order);
        model.addAttribute("orderDetails", orderDetailsDTOs);
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
