package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class carts {
    
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customerId", nullable = false)
    private int customerId;

    @Column(name = "productId", nullable = false)
    private int productId;

    @ManyToOne
    @JoinColumn(name = "customerId", insertable = false, updatable = false)
    private customers customer;

    @ManyToOne
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private products product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public customers getCustomer() {
        return customer;
    }

    public void setCustomer(customers customer) {
        this.customer = customer;
        this.customerId = customer.getCustomerId();
    }

    public products getProduct() {
        return product;
    }

    public void setProduct(products product) {
        this.product = product;
        this.productId = product.getProductId();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}