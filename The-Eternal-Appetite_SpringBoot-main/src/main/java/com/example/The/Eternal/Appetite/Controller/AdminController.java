package com.example.The.Eternal.Appetite.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.The.Eternal.Appetite.Repository.CategoryRepository;
import com.example.The.Eternal.Appetite.Repository.ContactMessageRepository;
import com.example.The.Eternal.Appetite.Repository.MenuItemRepository;
import com.example.The.Eternal.Appetite.Repository.OrderRepository;
import com.example.The.Eternal.Appetite.entity.ContactMessage;
import com.example.The.Eternal.Appetite.entity.MenuItem;
import com.example.The.Eternal.Appetite.entity.Order;

@Controller
public class AdminController {
	
	  @Autowired
	    private MenuItemRepository menuRepo;

	    @Autowired
	    private CategoryRepository categoryRepo;
	    
	    
	    
	    @Autowired
	    private OrderRepository orderRepository;
	    
	    @Autowired
	    private ContactMessageRepository contactMessageRepository;
	    
	    
	    @GetMapping("/admin/dashboard")
	    public String adminDashboard(Model model) {
	        model.addAttribute("menuItems", menuRepo.findAll()); // Optional: show menu items
	        return "admin-dashboard"; // 👈 Yeh file templates folder me honi chahiye
	    }


	   // Show add menu item form
    @GetMapping("/admin/menu/add")
    public String showAddMenuForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin-add-menu";
    }

    @GetMapping("/admin/menu/edit/{id}")
    public String showEditMenuForm(@ModelAttribute("id") Long id, Model model) {
        MenuItem item = menuRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Menu Item ID: " + id));
        
        model.addAttribute("menuItem", item);
        model.addAttribute("categories", categoryRepo.findAll());
        return "admin-edit-menu"; // 👈 ye HTML page banana padega
    }

    
    @PostMapping("/admin/menu/edit/{id}")
    public String updateMenuItem(@PathVariable Long id, @ModelAttribute MenuItem menuItem) {
        menuItem.setId(id); // Ensure same ID updated
        menuRepo.save(menuItem); // Updated info save hota hai
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/menu/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuRepo.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/menu/add")
    public String addMenuItem(@ModelAttribute MenuItem menuItem) {
        menuRepo.save(menuItem);
        return "redirect:/admin/dashboard"; // ✅ ✅ ✅
    }

    @GetMapping("/admin/orders")
    public String viewAllOrders(Model model) {
        List<Order> allOrders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderTime")); // latest first
        model.addAttribute("orders", allOrders);
        return "admin_order_list";
    }

    @PostMapping("/admin/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
        return "redirect:/admin/orders";
    }
    
    @GetMapping("/admin/order/details/{id}")
    public String viewOrderDetails(@PathVariable("id") Long id, Model model) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            model.addAttribute("order", order);
            return "admin_order_detail"; // ✅ this matches your HTML filename
        } else {
            return "redirect:/admin/orders"; // fallback if order not found
        }
    }
    
   

    @GetMapping("/admin/contact-messages")
    public String viewContactMessages(Model model) {
        List<ContactMessage> messages = contactMessageRepository.findAll();
        model.addAttribute("messages", messages);
        return "admin_contact_messages";
    }
    
    
    @PostMapping("/admin/contact-messages/delete/{id}")
    public String deleteContactMessage(@PathVariable Long id) {
        contactMessageRepository.deleteById(id);
        return "redirect:/admin/contact-messages";
    }
}
