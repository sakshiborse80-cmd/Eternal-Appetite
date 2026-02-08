package com.example.The.Eternal.Appetite.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.The.Eternal.Appetite.entity.Order;
import com.example.The.Eternal.Appetite.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerName(String customerName);
    List<Order> findByCustomerNameOrderByOrderTimeDesc(String customerName);
    List<Order> findTop3ByCustomerNameOrderByOrderTimeDesc(String customerName);
    List<Order> findByMobileOrderByOrderTimeDesc(String mobile);

    // ✅ Add this new method to get orders for a specific user
    List<Order> findByUserOrderByOrderTimeDesc(User user);
    List<Order> findByUserId(Long userId);

}
