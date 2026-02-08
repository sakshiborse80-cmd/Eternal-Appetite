package com.example.The.Eternal.Appetite.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class CartItem implements Serializable {
    private Long menuItemId;
    private String name;
    private double price;
    private int quantity;

    public CartItem() {
        // No-arg constructor for session & JSON
    }

    public CartItem(Long menuItemId, String name, double price, int quantity) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return this.quantity * this.price;
    }
}
