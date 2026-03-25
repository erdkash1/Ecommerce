package com.iggy.ecommerce.repository;

import com.iggy.ecommerce.entity.Order;
import com.iggy.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByUser(User user);
}
