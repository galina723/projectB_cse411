package com.example.demo.model;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String brand;
    private String description;
    private String image;
    private String price;
    private String category;
    private String size;
    private String color;
    private String stock;
    private String rating;
    private String discount;
    private String delivery;


}
