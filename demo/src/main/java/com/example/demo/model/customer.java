package com.example.demo.model;
import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    


}
