package com.example.The.Eternal.Appetite.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.The.Eternal.Appetite.Repository.MenuItemRepository;
import com.example.The.Eternal.Appetite.Repository.OrderItemRepository;
import com.example.The.Eternal.Appetite.Repository.OrderRepository;
import com.example.The.Eternal.Appetite.Validation.CheckoutDTO;
import com.example.The.Eternal.Appetite.entity.CartItem;
import com.example.The.Eternal.Appetite.entity.MenuItem;
import com.example.The.Eternal.Appetite.entity.Order;
import com.example.The.Eternal.Appetite.entity.OrderItem;
import com.example.The.Eternal.Appetite.entity.User;
import java.time.LocalDateTime; // ✅ Add this

import jakarta.servlet.http.HttpSession;

@Controller
	public class OrderController {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	 @Autowired
	    private MenuItemRepository menuItemRepository;


	
	
	 @GetMapping("/checkout")
	 public String showCheckoutForm(HttpSession session, Model model) {
	     User user = (User) session.getAttribute("user");
	     if (user == null) {
	         return "redirect:/login";
	     }

	     // ✅ Get cart from session
	     List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	     if (cart != null && !cart.isEmpty()) {
	         double total = 0.0;
	         for (CartItem item : cart) {
	             total += item.getPrice() * item.getQuantity();
	         }
	         session.setAttribute("cartTotal", total); // store in session
	     } else {
	         session.setAttribute("cartTotal", 0.0);
	     }

	     // ✅ Also add cartTotal to model for Thymeleaf
	     model.addAttribute("cartTotal", session.getAttribute("cartTotal"));

	     return "checkout";
	 }


	 
	 
	 @PostMapping("/place-order")
	 @ResponseBody
	 public String placeOrder(HttpSession session) {
	     CheckoutDTO dto = (CheckoutDTO) session.getAttribute("reviewCheckoutDTO");
	     User user = (User) session.getAttribute("user");
	     List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	     if (dto == null || user == null || cart == null || cart.isEmpty()) {
	         return "fail";
	     }

	     Order order = new Order();
	     order.setCustomerName(user.getName());
	     order.setMobile(user.getMobile());
	     order.setAddress(dto.getAddress());
	     order.setPaymentMethod(dto.getPaymentMethod());
	     order.setStatus("Pending");

	     Double total = (Double) session.getAttribute("cartTotal");
	     order.setTotalPrice(total != null ? total : 0.0);
	     order.setUser(user);
	     order.setOrderTime(LocalDateTime.now());

	     // ✅ Save order first (so we can link orderItems)
	     orderRepository.save(order);

	     // ✅ Convert CartItems to OrderItems
	     List<OrderItem> orderItems = new ArrayList<>();

	     for (CartItem cartItem : cart) {
	         OrderItem orderItem = new OrderItem();
	         orderItem.setQuantity(cartItem.getQuantity());
	         orderItem.setPrice(cartItem.getPrice());
	         orderItem.setTotalPrice(cartItem.getTotalPrice());
	         orderItem.setItemName(cartItem.getName());
	         orderItem.setOrder(order);

	         // ✅ Fetch MenuItem by ID
	         MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId()).orElse(null);
	         orderItem.setMenuItem(menuItem);

	         orderItems.add(orderItem);
	     }

	     // ✅ Attach and save orderItems
	     order.setOrderItems(orderItems);
	     orderRepository.save(order); // save again with items

	     // ✅ Clear cart after placing order
	     session.removeAttribute("cart");
	     session.removeAttribute("cartTotal");

	     // ✅ Save for confirmation page
	     session.setAttribute("placedOrder", order);

	     return "success";
	 }



	 @GetMapping("/show-confirmation")
	 public String showConfirmation(HttpSession session, Model model) {
	     Order placedOrder = (Order) session.getAttribute("placedOrder");

	     if (placedOrder != null) {
	         model.addAttribute("customerName", placedOrder.getCustomerName());
	         model.addAttribute("orderId", placedOrder.getId());
	         model.addAttribute("totalPrice", placedOrder.getTotalPrice());
	         model.addAttribute("status", placedOrder.getStatus());
	         model.addAttribute("paymentMethod", placedOrder.getPaymentMethod());
	     } else {
	         model.addAttribute("customerName", "Guest");
	         model.addAttribute("orderId", "N/A");
	         model.addAttribute("totalPrice", "0.00");
	         model.addAttribute("status", "Unknown");
	         model.addAttribute("paymentMethod", "N/A");
	     }

	     return "order-confirmation";
	 }


	 
	 @GetMapping("/order/history")
	 public String viewOrderHistory(Model model, HttpSession session) {
	     User loggedInUser = (User) session.getAttribute("user"); // 🔁 Changed this line
	     if (loggedInUser == null) {
	         return "redirect:/login";
	     }

	     List<Order> orders = orderRepository.findByUserId(loggedInUser.getId());
	     model.addAttribute("orders", orders);

	     return "order_history";
	 }

	 
	 @PostMapping("/review-order")
	 public String reviewOrder(@ModelAttribute CheckoutDTO checkoutDTO, HttpSession session, Model model) {
	     // ✅ Store in session temporarily
	     session.setAttribute("reviewCheckoutDTO", checkoutDTO);

	     Order tempOrder = new Order();
	     User user = (User) session.getAttribute("user");
	     tempOrder.setCustomerName(user.getName());
	     tempOrder.setMobile(user.getMobile());
	     tempOrder.setAddress(checkoutDTO.getAddress());
	     tempOrder.setPaymentMethod(checkoutDTO.getPaymentMethod());
	     tempOrder.setStatus("Pending");

	     Double totalPrice = (Double) session.getAttribute("cartTotal");
	     tempOrder.setTotalPrice(totalPrice != null ? totalPrice : 0.0);
	     model.addAttribute("order", tempOrder);

	     // ✅ Also pass cart items
	     List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
	     model.addAttribute("cartItems", cartItems);

	     return "order-summary";  // this page will have a "Confirm & Pay" button
	 }

	 @PostMapping("/confirm-order")
	 public String confirmAndPay(HttpSession session, Model model) {
	     CheckoutDTO checkoutDTO = (CheckoutDTO) session.getAttribute("reviewCheckoutDTO");

	     if (checkoutDTO == null) {
	         return "redirect:/checkout";
	     }

	     // Set order object again for summary
	     Order tempOrder = new Order();
	     User user = (User) session.getAttribute("user");

	     tempOrder.setCustomerName(user.getName());
	     tempOrder.setMobile(user.getMobile());
	     tempOrder.setAddress(checkoutDTO.getAddress());
	     tempOrder.setPaymentMethod(checkoutDTO.getPaymentMethod());
	     tempOrder.setStatus("Pending");

	     Double totalPrice = (Double) session.getAttribute("cartTotal");
	     tempOrder.setTotalPrice(totalPrice != null ? totalPrice : 0.0);
	     model.addAttribute("order", tempOrder);

	     List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
	     model.addAttribute("cartItems", cartItems);

	     return "order-summary";  // Razorpay will trigger from here if ONLINE
	 }

	 @PostMapping("/finalize-order")
	 @ResponseBody
	 public String finalizeOrder(HttpSession session) {
	     System.out.println("✅ finalizeOrder called");

	     CheckoutDTO checkoutDTO = (CheckoutDTO) session.getAttribute("reviewCheckoutDTO");
	     User user = (User) session.getAttribute("user");
	     List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	     if (checkoutDTO == null || user == null || cart == null || cart.isEmpty()) {
	         System.out.println("❌ Missing data");
	         return "error";
	     }

	     try {
	         Order order = new Order();
	         order.setCustomerName(user.getName());
	         order.setMobile(user.getMobile());
	         order.setAddress(checkoutDTO.getAddress());
	         order.setPaymentMethod("ONLINE");
	         order.setStatus("Pending");
	         order.setOrderTime(LocalDateTime.now());
	         order.setUser(user);

	         Double totalPrice = (Double) session.getAttribute("cartTotal");
	         order.setTotalPrice(totalPrice != null ? totalPrice : 0.0);

	         orderRepository.save(order); // save first to generate order ID

	         List<OrderItem> orderItems = new ArrayList<>();
	         for (CartItem cartItem : cart) {
	             OrderItem orderItem = new OrderItem();
	             orderItem.setQuantity(cartItem.getQuantity());
	             orderItem.setPrice(cartItem.getPrice());
	             orderItem.setTotalPrice(cartItem.getTotalPrice());
	             orderItem.setItemName(cartItem.getName());
	             orderItem.setOrder(order); // set foreign key

	             MenuItem menuItem = menuItemRepository.findById(cartItem.getMenuItemId()).orElse(null);
	             orderItem.setMenuItem(menuItem);

	             orderItems.add(orderItem);
	         }

	         order.setOrderItems(orderItems);
	         orderRepository.save(order); // save again to persist items

	         session.removeAttribute("cart");
	         session.removeAttribute("cartTotal");
	         session.setAttribute("placedOrder", order);

	         System.out.println("✅ Order with items saved, ID: " + order.getId());
	         return "success";

	     } catch (Exception e) {
	         e.printStackTrace();
	         return "error";
	     }
	 }

	 }








	
	

	


