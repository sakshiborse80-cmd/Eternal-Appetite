package com.example.The.Eternal.Appetite.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.The.Eternal.Appetite.Repository.MenuItemRepository;
import com.example.The.Eternal.Appetite.Repository.UserRepository;
import com.example.The.Eternal.Appetite.entity.CartItem;
import com.example.The.Eternal.Appetite.entity.MenuItem;
import com.example.The.Eternal.Appetite.entity.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // Show register page
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }
        user.setRole("user");
        userRepository.save(user);
        model.addAttribute("message", "Registered successfully!");
        return "login";
    }

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Handle login
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
        session.setAttribute("user", user);
        if ("admin".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }
    
   
    

    

    
    

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();            // Clear session
        return "redirect:/";             // 👈 Redirect to home page
    }



   

   

}

