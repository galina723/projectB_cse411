package com.example.demo.model;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Products {

    @Id
    @Column(name = "id", length = 10, nullable = false)
    private String ProductId;

    @Column(name = "name", length = 50, nullable = false)
    private String ProductName;

    @Column(name = "category", length = 30, nullable = false)
    private String ProductCategory;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String ProductDescription;

    @Column(name = "mainimage", length = 2083, nullable = false)
    private String ProductMainImage;

    @Column(name = "otherimages", length = 2083, nullable = false)
    private String ProductOtherImages;

    @Column(name = "price", nullable = false)
    private double ProductPrice;

    @Column(name = "quantity", nullable = false)
    private int ProductQuantity;

    @CreationTimestamp
    @Column(name = "createtime", updatable = false)
    private LocalDateTime CreateTime;

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getProductMainImage() {
        return ProductMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        ProductMainImage = productMainImage;
    }

    public String getProductOtherImages() {
        return ProductOtherImages;
    }

    public void setProductOtherImages(String productOtherImages) {
        ProductOtherImages = productOtherImages;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public LocalDateTime getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        CreateTime = createTime;
    }
}
