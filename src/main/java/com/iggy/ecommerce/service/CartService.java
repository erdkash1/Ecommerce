package com.iggy.ecommerce.service;

import com.iggy.ecommerce.entity.Cart;
import com.iggy.ecommerce.entity.CartItem;
import com.iggy.ecommerce.entity.Product;
import com.iggy.ecommerce.entity.User;
import com.iggy.ecommerce.repository.CartItemRepository;
import com.iggy.ecommerce.repository.CartRepository;
import com.iggy.ecommerce.repository.ProductRepository;
import com.iggy.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItemToCart(Long userId, Long productId, Integer quantity){
        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));        if (!product.isInStock()) {
            throw new RuntimeException("Product is out of stock");
        }
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPriceAtTime(product.getPrice());
        cart.addItem(cartItem);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cart.removeItem(cartItem);
        return cartRepository.save(cart);
    }

    public Cart clearCart(Long cartId){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();
        return cartRepository.save(cart);
    }


}
