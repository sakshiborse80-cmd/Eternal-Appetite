package com.example.The.Eternal.Appetite.Service;

import com.example.The.Eternal.Appetite.Repository.MenuItemRepository;
import com.example.The.Eternal.Appetite.entity.CartItem;
import com.example.The.Eternal.Appetite.entity.MenuItem;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public void addToCart(Long menuItemId, HttpSession session) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElse(null);
        if (menuItem == null) return;

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        boolean found = false;
        for (CartItem item : cart) {
            if (item.getMenuItemId().equals(menuItem.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = new CartItem(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), 1);
            cart.add(newItem);
        }

        session.setAttribute("cart", cart);
    }

    public void removeFromCart(Long menuItemId, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getMenuItemId().equals(menuItemId));
            session.setAttribute("cart", cart);
        }
    }

    public double calculateTotal(List<CartItem> cart) {
        return cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}