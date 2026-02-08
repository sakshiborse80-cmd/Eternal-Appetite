package com.example.The.Eternal.Appetite.entity;

	import jakarta.persistence.*;
	import lombok.*;

	@Entity
	@Data
		public class Category {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(unique = true)
	    private String name;  // e.g., Drinks, Fast Food, Desserts
	}


