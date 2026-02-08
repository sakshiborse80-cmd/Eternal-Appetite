package com.example.The.Eternal.Appetite.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.The.Eternal.Appetite.Service.CartService;
import com.example.The.Eternal.Appetite.entity.CartItem;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ Add item to cart (with login check)
    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Object user = session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please login to add items to cart.");
            return "redirect:/login";
        }

        cartService.addToCart(id, session);
        redirectAttributes.addFlashAttribute("message", "Item added to cart!");
        return "redirect:/cart";
    }

    // ✅ Show cart page
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        double totalAmount = 0;
        for (CartItem item : cart) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);
        return "cart";
    }

    // ✅ Remove item from cart
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.removeIf(item -> item.getMenuItemId().equals(id));
            session.setAttribute("cart", cart);
            redirectAttributes.addFlashAttribute("message", "Item removed from cart!");
        }

        return "redirect:/cart";
    }

    // ✅ Increase item quantity
    @GetMapping("/cart/increase/{id}")
    public String increaseQuantity(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getMenuItemId().equals(id)) {
                    item.setQuantity(item.getQuantity() + 1);
                    break;
                }
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    // ✅ Decrease item quantity
    @GetMapping("/cart/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (int i = 0; i < cart.size(); i++) {
                CartItem item = cart.get(i);
                if (item.getMenuItemId().equals(id)) {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                    } else {
                        cart.remove(i);
                    }
                    break;
                }
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }
    
   

    @PostMapping("/cart/update-quantity/{id}/{action}")
    public String updateQuantity(@PathVariable Long id, @PathVariable String action, HttpSession session, RedirectAttributes redirectAttributes) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getMenuItemId().equals(id)) {
                    if ("increase".equals(action)) {
                        item.setQuantity(item.getQuantity() + 1);
                    } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                    }
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }
    @GetMapping("/cart/empty")
    public String emptyCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

}
