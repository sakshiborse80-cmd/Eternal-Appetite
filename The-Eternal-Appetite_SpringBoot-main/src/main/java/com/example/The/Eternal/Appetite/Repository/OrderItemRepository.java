package com.example.The.Eternal.Appetite.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.The.Eternal.Appetite.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	    // optional: add queries to fetch by orderId
	}


