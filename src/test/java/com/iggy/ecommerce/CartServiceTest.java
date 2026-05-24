package com.iggy.ecommerce;

import com.iggy.ecommerce.entity.Cart;
import com.iggy.ecommerce.entity.CartItem;
import com.iggy.ecommerce.entity.Product;
import com.iggy.ecommerce.entity.User;
import com.iggy.ecommerce.exception.BadRequestException;
import com.iggy.ecommerce.exception.ResourceNotFoundException;
import com.iggy.ecommerce.repository.CartItemRepository;
import com.iggy.ecommerce.repository.CartRepository;
import com.iggy.ecommerce.repository.ProductRepository;
import com.iggy.ecommerce.repository.UserRepository;
import com.iggy.ecommerce.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddItemToCartSuccessfully() {
        // Given
        Long userId = 1L;
        Long productId = 1L;
        Integer quantity = 2;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setName("iPhone 15");
        product.setPrice(new BigDecimal("999.99"));
        product.setStock(50);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        Cart result = cartService.addItemToCart(userId, productId, quantity);

        // Then
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenProductOutOfStock() {
        // Given
        Long userId = 1L;
        Long productId = 1L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setStock(0);

        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When & Then
        assertThrows(BadRequestException.class,
                () -> cartService.addItemToCart(userId, productId, 1));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        Long userId = 1L;
        Long productId = 99L;

        User user = new User();
        user.setId(userId);

        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> cartService.addItemToCart(userId, productId, 1));
    }
}