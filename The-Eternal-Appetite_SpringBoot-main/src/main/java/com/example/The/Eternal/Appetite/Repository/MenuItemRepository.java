package com.example.The.Eternal.Appetite.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.The.Eternal.Appetite.entity.Category;
import com.example.The.Eternal.Appetite.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByNameContainingIgnoreCase(String keyword);
    List<MenuItem> findByCategoryId(Long categoryId);
    List<MenuItem> findByCategory(Category category);
    List<MenuItem> findByPrice(double price);


}
