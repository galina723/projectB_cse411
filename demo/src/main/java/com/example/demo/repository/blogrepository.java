package com.example.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.blogs;

public interface blogrepository extends CrudRepository<blogs, Integer> {

    @Query(value = "SELECT COALESCE(MAX(id), 0) + 1 FROM  blogs", nativeQuery = true)
    int findNextBlogId();

}
