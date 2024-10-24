package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orderdetails")
public class orderdetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "orderid", length = 10, nullable = false)
    private int OrderId;

    @Column (name = "productid", length = 10, nullable = false)
    private String ProductId;

    @Column (name = "price", nullable = false)
    private int ProductPrice;

    @Column (name = "quantity", nullable = false)
    private int ProductQuantity;

    @Column (name = "coupon", length = 50, nullable = false)
    private String coupon;
    public int getOrderId() {
        return OrderId;
    }
    public void setOrderId(int orderId) {
        OrderId = orderId;
    }
    public String getProductId() {
        return ProductId;
    }
    public void setProductId(String productId) {
        ProductId = productId;
    }
    public int getProductPrice() {
        return ProductPrice;
    }
    public void setProductPrice(int productPrice) {
        ProductPrice = productPrice;
    }
    public int getProductQuantity() {
        return ProductQuantity;
    }
    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }
    public String getCoupon() {
        return coupon;
    }
    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

}
