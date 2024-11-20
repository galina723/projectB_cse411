package com.example.demo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.admin;

public interface adminrepository extends CrudRepository<admin, Integer> {

    @Query("SELECT a FROM admin a WHERE a.adminName = ?1")
    admin findByAdminName(String adminName);

}
