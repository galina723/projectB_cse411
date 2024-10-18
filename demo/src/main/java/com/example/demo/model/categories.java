package com.example.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class categories {

    @Id
    @Column(name = "id", length = 10, nullable = false)
    private String CategoryId;

    @Column(name = "name", length = 50, nullable = false)
    private String CategoryName;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String CategoryDescription;

    @CreationTimestamp
    @Column(name = "createtime", updatable = false)
    private LocalDateTime createdate;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        CategoryDescription = categoryDescription;
    }

    public LocalDateTime getCreatedate() {
        return createdate;
    }

    public void setCreatedate(LocalDateTime createdate) {
        this.createdate = createdate;
    }

}
