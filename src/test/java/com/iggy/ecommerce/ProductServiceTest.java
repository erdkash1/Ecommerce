package com.iggy.ecommerce.service;

import com.iggy.ecommerce.entity.Product;
import com.iggy.ecommerce.exception.ResourceNotFoundException;
import com.iggy.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Laptop");
        testProduct.setDescription("A great laptop");
        testProduct.setPrice(BigDecimal.valueOf(999.99));
        testProduct.setStock(10);
        testProduct.setCategory("Electronics");
    }


    @Test
    void shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Test Laptop", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoProducts() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }


    @Test
    void shouldReturnProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Laptop", result.get().getName());
        assertEquals(BigDecimal.valueOf(999.99), result.get().getPrice());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(99L);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(99L);
    }


    @Test
    void shouldReturnProductsByCategory() {
        when(productRepository.findByCategory("Electronics"))
                .thenReturn(List.of(testProduct));

        List<Product> result = productService.getProductsByCategory("Electronics");

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        verify(productRepository, times(1)).findByCategory("Electronics");
    }

    @Test
    void shouldReturnEmptyListForUnknownCategory() {
        when(productRepository.findByCategory("Unknown"))
                .thenReturn(List.of());

        List<Product> result = productService.getProductsByCategory("Unknown");

        assertTrue(result.isEmpty());
    }


    @Test
    void shouldCreateProduct() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals("Test Laptop", result.getName());
        assertEquals(BigDecimal.valueOf(999.99), result.getPrice());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void shouldSaveProductWithCorrectDetails() {
        Product newProduct = new Product();
        newProduct.setName("New Phone");
        newProduct.setPrice(BigDecimal.valueOf(599.99));
        newProduct.setStock(20);
        newProduct.setCategory("Mobile");

        when(productRepository.save(newProduct)).thenReturn(newProduct);

        Product result = productService.createProduct(newProduct);

        assertEquals("New Phone", result.getName());
        assertEquals(20, result.getStock());
        verify(productRepository, times(1)).save(newProduct);
    }


    @Test
    void shouldUpdateProductSuccessfully() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Laptop");
        updatedDetails.setDescription("Updated description");
        updatedDetails.setPrice(BigDecimal.valueOf(1099.99));
        updatedDetails.setStock(5);
        updatedDetails.setCategory("Electronics");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.updateProduct(1L, updatedDetails);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentProduct() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Product");

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(99L, updatedDetails)
        );

        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).save(any());
    }


    @Test
    void shouldDeleteProductById() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldCallDeleteWithCorrectId() {
        doNothing().when(productRepository).deleteById(42L);

        productService.deleteProduct(42L);

        verify(productRepository, times(1)).deleteById(42L);
        verify(productRepository, never()).deleteById(1L);
    }
}