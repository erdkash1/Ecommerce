package com.iggy.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
