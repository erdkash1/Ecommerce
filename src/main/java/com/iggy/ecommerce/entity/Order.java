package com.iggy.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    private Double totalPrice;
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public Double getTotalPrice() {return totalPrice;}
    public void setTotalPrice(Double totalPrice) {this.totalPrice = totalPrice;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
