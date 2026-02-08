package com.example.The.Eternal.Appetite.entity;
import jakarta.persistence.*;
	import lombok.*;
	import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

	@Entity
	@Data
	public class ContactMessage {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    private String email;

	    private String message;

	    private LocalDateTime submittedAt = LocalDateTime.now();
	    
	    
	    public String getFormattedSubmittedAt() {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	        return submittedAt.format(formatter);
	    }
	}


