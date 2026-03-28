package com.iggy.ecommerce.service;

import com.iggy.ecommerce.entity.*;
import com.iggy.ecommerce.exception.BadRequestException;
import com.iggy.ecommerce.exception.ResourceNotFoundException;
import com.iggy.ecommerce.repository.CartRepository;
import com.iggy.ecommerce.repository.OrderRepository;
import com.iggy.ecommerce.repository.ProductRepository;
import com.iggy.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository,
                        UserRepository userRepository){
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Order placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
            if (cart.getItems().isEmpty()) {
                throw new BadRequestException("Cart is empty");
            }
            Order order = new Order();
            order.setUser(user);
            BigDecimal total = BigDecimal.ZERO;
            for (CartItem cartItem : cart.getItems()) {
               OrderItem orderItem = new OrderItem();
               orderItem.setProduct(cartItem.getProduct());
               orderItem.setQuantity(cartItem.getQuantity());
               orderItem.setPriceAtTime(cartItem.getPriceAtTime());
               order.addItem(orderItem);
               total = total.add(cartItem.getSubtotal());

            }
            order.setTotalPrice(total);
            Order savedOrder = orderRepository.save(order);
            cart.getItems().clear();
            cartRepository.save(cart);
            return savedOrder;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }


    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }



    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        existing.setStatus(status);
        return orderRepository.save(existing);

    }


}
