package com.iggy.ecommerce.repository;

import com.iggy.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Product findByCategory(String category);
}
