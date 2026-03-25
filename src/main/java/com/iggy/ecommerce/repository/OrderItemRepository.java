package com.iggy.ecommerce.repository;

import com.iggy.ecommerce.entity.Order;
import com.iggy.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    public List<OrderItem> findByOrder(Order order);
}
