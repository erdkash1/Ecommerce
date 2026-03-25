package com.iggy.ecommerce.repository;

import com.iggy.ecommerce.entity.Cart;
import com.iggy.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findByUser(User user);
}
