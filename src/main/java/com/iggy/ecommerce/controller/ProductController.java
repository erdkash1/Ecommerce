package com.iggy.ecommerce.controller;

import com.iggy.ecommerce.entity.Product;
import com.iggy.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @GetMapping
    public List<Product> getAllProduct(){ return productService.getAllProducts();}

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id){ return productService.getProductById(id);}

    @GetMapping("/category/{category}")
    public List<Product> getProductCategory(@PathVariable String category){ return productService.getProductsByCategory(category);}

    @PostMapping
    public Product createProduct(@RequestBody Product product){ return productService.createProduct(product);}

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product){ return productService.updateProduct(id, product);}

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){ productService.deleteProduct(id);}


}
