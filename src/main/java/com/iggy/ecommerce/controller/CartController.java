package com.iggy.ecommerce.controller;

import com.iggy.ecommerce.entity.Cart;
import com.iggy.ecommerce.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public Cart getCartById(@PathVariable Long userId){ return cartService.getCartByUserId(userId);}

    @PostMapping("/{userId}/items")
    public Cart addItemToCart(@PathVariable Long userId, @RequestParam Long productId,@RequestParam Integer quantity){ return cartService.addItemToCart(userId, productId, quantity);}

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public void removeItemFromCart(@PathVariable Long userId, @PathVariable Long cartItemId){
        cartService.removeItemFromCart(userId, cartItemId);
    }

    @DeleteMapping("/{cartId}/clear")
    public void clearCart(@PathVariable Long cartId){ cartService.clearCart(cartId);}

}
