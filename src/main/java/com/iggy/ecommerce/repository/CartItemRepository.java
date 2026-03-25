package com.iggy.ecommerce.repository;

import com.iggy.ecommerce.entity.Cart;
import com.iggy.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public List<CartItem> findByCart(Cart cart);
}
