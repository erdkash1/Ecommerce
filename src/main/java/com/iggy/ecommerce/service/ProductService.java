package com.iggy.ecommerce.service;

import com.iggy.ecommerce.entity.Product;
import com.iggy.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }




    public List<Product> getAllProducts(){ return productRepository.findAll();}
    public Optional<Product> getProductById(Long id){ return productRepository.findById(id);}
    public List<Product> getProductsByCategory(String category){ return productRepository.findByCategory(category);}
    public Product createProduct(Product product){ return productRepository.save(product);}
    public Product updateProduct(Long id, Product product) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setCategory(product.getCategory());
        return productRepository.save(existing);
    }
    public void deleteProduct(Long id){ productRepository.deleteById(id);}
}
