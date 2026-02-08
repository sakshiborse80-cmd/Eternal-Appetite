package com.example.The.Eternal.Appetite.Controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class RazorpayOrderController {

    // Razorpay keys from environment variables
    @Value("${RAZORPAY_KEY_ID}")
    private String keyId;

    @Value("${RAZORPAY_KEY_SECRET}")
    private String keySecret;

    @PostMapping("/create-razorpay-order")
    public Map<String, Object> createOrder(@RequestParam("amount") double amount) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Initialize Razorpay client with environment variables
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            // Convert amount to paise
            int amountInPaise = (int) (amount * 100);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + UUID.randomUUID());

            Order order = razorpay.orders.create(orderRequest);

            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Error creating Razorpay order: " + e.getMessage());
        }

        return response;
    }
}
