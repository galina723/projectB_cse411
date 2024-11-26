package com.example.demo.repository;

import com.example.demo.model.products;

import java.util.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface productrepository extends CrudRepository<products, Integer> {
    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM product", nativeQuery = true)
    int findNextProductId();

    @Query("SELECT p FROM products p WHERE p.ProductCategory = ?1")
    List<products> findProductsByCategory(String ProductCategory);

    @Query("SELECT p FROM products p WHERE p.ProductCategory = ?1 AND p.ProductId != ?2")
    List<products> findProductsByCategoryExcludingId(String productCategory, int productId);

    @Query("SELECT p FROM products p WHERE p.ProductCategory = (SELECT c.CategoryName FROM categories c WHERE c.CategoryId = ?1) ORDER BY p.ProductId ASC")
    List<products> findProductsByCategoryId(int categoryId, Pageable pageable);

    @Query("SELECT p FROM products p ORDER BY p.CreateTime DESC")
    List<products> findTop5Products(Pageable pageable);

    @Query("SELECT p FROM products p JOIN orderdetails od ON p.ProductId = od.ProductId " +
            "GROUP BY p ORDER BY SUM(od.ProductQuantity) DESC")
    List<products> findBestProducts(Pageable pageable);
}
