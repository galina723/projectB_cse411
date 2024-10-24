package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 10, nullable = false)
    private int CategoryId;

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    @Column(name = "name", length = 50, nullable = false)
    private String CategoryName;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    @Column (name = "status")
    private Boolean CategoryStatus;

    public Boolean getCategoryStatus() {
        return CategoryStatus;
    }

    public void setCategoryStatus(Boolean categoryStatus) {
        CategoryStatus = categoryStatus;
    }

    

    

}
