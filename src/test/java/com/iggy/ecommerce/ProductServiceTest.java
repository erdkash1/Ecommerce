package com.iggy.ecommerce;

import com.iggy.ecommerce.entity.Product;
import com.iggy.ecommerce.exception.ResourceNotFoundException;
import com.iggy.ecommerce.repository.ProductRepository;
import com.iggy.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfully() {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setName("iPhone 15");
        product.setDescription("Latest Apple smartphone");
        product.setPrice(new BigDecimal("999.99"));
        product.setStock(50);
        product.setCategory("Electronics");

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product result = productService.createProduct(product);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("iPhone 15", result.getName());
        assertEquals(new BigDecimal("999.99"), result.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        Optional<Product> result = productService.getProductById(productId);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("iPhone 15");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Samsung Galaxy");

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> results = productService.getAllProducts();

        assertEquals(2, results.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        // Given
        Long productId = 99L;
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(productId, product));
    }
}