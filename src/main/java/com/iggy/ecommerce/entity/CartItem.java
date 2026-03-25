package com.iggy.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Integer getQuantity() {return quantity;}
    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public Cart getCart() {return cart;}
    public void setCart(Cart cart) {this.cart = cart;}

    public Product getProduct() {return product;}
    public void setProduct(Product product) {this.product = product;}
}
