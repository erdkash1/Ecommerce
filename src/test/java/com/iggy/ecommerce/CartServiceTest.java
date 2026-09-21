package com.iggy.ecommerce.service;

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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("iggy@gmail.com");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Laptop");
        testProduct.setPrice(BigDecimal.valueOf(999.99));
        testProduct.setStock(10);

        testCart = new Cart();
        testCart.setId(1L);
        testCart.setUser(testUser);
        testCart.setItems(new ArrayList<>());

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);
        testCartItem.setPriceAtTime(BigDecimal.valueOf(999.99));
    }


    @Test
    void shouldReturnExistingCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        Cart result = cartService.getCartByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldCreateNewCartWhenNoneExists() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.getCartByUserId(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForNewCart() {
        when(cartRepository.findByUserId(99L)).thenReturn(Optional.empty());
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                cartService.getCartByUserId(99L)
        );

        verify(cartRepository, never()).save(any());
    }


    @Test
    void shouldAddItemToCartSuccessfully() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.addItemToCart(1L, 1L, 2);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                cartService.addItemToCart(1L, 99L, 1)
        );

        verify(cartRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenProductOutOfStock() {
        testProduct.setStock(0);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThrows(BadRequestException.class, () ->
                cartService.addItemToCart(1L, 1L, 1)
        );

        verify(cartRepository, never()).save(any());
    }

    @Test
    void shouldNotSaveCartWhenProductOutOfStock() {
        testProduct.setStock(0);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThrows(BadRequestException.class, () ->
                cartService.addItemToCart(1L, 1L, 2)
        );

        verify(cartRepository, never()).save(any(Cart.class));
    }


    @Test
    void shouldRemoveItemFromCartSuccessfully() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.removeItemFromCart(1L, 1L);

        assertNotNull(result);
        verify(cartItemRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenCartItemNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                cartService.removeItemFromCart(1L, 99L)
        );

        verify(cartRepository, never()).save(any());
    }


    @Test
    void shouldClearCartSuccessfully() {
        testCart.getItems().add(testCartItem);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.clearCart(1L);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void shouldThrowExceptionWhenCartNotFoundForClear() {
        when(cartRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                cartService.clearCart(99L)
        );

        verify(cartRepository, never()).save(any());
    }

    @Test
    void shouldClearAllItemsFromCart() {
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        testCart.getItems().add(item1);
        testCart.getItems().add(item2);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        cartService.clearCart(1L);

        assertTrue(testCart.getItems().isEmpty());
    }
}