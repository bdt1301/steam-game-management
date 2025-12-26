package com.user.steammgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.steammgmt.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
