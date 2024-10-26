package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class productimages {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ImageId;  // Unique identifier for the image record

    private int ProductId; // Reference to the Product

    private String mainImage;
    private String otherImage1;
    private String otherImage2;
    private String otherImage3;
    private String otherImage4;

    // Getters and setters
    public int getId() {
        return ImageId;
    }

    public void setId(int ImageId) {
        this.ImageId = ImageId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(Integer ProductId) {
        this.ProductId = ProductId;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getOtherImage1() {
        return otherImage1;
    }

    public void setOtherImage1(String otherImage1) {
        this.otherImage1 = otherImage1;
    }

    public String getOtherImage2() {
        return otherImage2;
    }

    public void setOtherImage2(String otherImage2) {
        this.otherImage2 = otherImage2;
    }

    public String getOtherImage3() {
        return otherImage3;
    }

    public void setOtherImage3(String otherImage3) {
        this.otherImage3 = otherImage3;
    }

    public String getOtherImage4() {
        return otherImage4;
    }

    public void setOtherImage4(String otherImage4) {
        this.otherImage4 = otherImage4;
    }
}