package com.iggy.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    private Integer stock;
    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getPrice() {return price;}
    public void setPrice(Double price) {this.price = price;}

    public Integer getStock() {return stock;}
    public void setStock(Integer stock) {this.stock = stock;}

    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
