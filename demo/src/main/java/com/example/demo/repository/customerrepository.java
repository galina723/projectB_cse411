package com.example.demo.repository;

import com.example.demo.model.customers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface customerrepository extends CrudRepository<customers, Integer> {
    customers findByCemail(String cemail);

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM product", nativeQuery = true)
    int findNextCustomerId();

    @Modifying
    @Transactional
    @Query("UPDATE customers c SET c.CustomerPassword = :newPassword WHERE c.cemail = :email")
    int updatePasswordByEmail(@Param("newPassword") String newPassword, @Param("email") String email);
    


}
