package com.example.demo.model;

import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private int OrderId;

    @Column(name = "customerid", length = 10, nullable = false)
    private String CustomerId;

    @CreationTimestamp
    @Column(name = "date", nullable = false)
    private Date OrderDate;

    public Date getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    @Column(name = "status", length = 50, nullable = false)
    private String OrderStatus;

    @Column(name = "amount", nullable = false)
    private int OrderAmount;

    @Column(name = "paymentmethod", length = 50, nullable = false)
    private String OrderPaymentMethod;

    @Column(name = "note", columnDefinition = "TEXT", length = 255, nullable = false)
    private String OrderNote;

    @Column(name = "address", length = 255, nullable = false)
    private String OrderAddress;

    @Column(name = "city", length = 50, nullable = false)
    private String OrderCity;

    @Column(name = "province", length = 50, nullable = false)
    private String OrderProvince;

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getOrderPaymentMethod() {
        return OrderPaymentMethod;
    }

    public void setOrderPaymentMethod(String orderPaymentMethod) {
        OrderPaymentMethod = orderPaymentMethod;
    }

    public String getOrderNote() {
        return OrderNote;
    }

    public void setOrderNote(String orderNote) {
        OrderNote = orderNote;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getOrderCity() {
        return OrderCity;
    }

    public void setOrderCity(String orderCity) {
        OrderCity = orderCity;
    }

    public String getOrderProvince() {
        return OrderProvince;
    }

    public void setOrderProvince(String orderProvince) {
        OrderProvince = orderProvince;
    }

}
