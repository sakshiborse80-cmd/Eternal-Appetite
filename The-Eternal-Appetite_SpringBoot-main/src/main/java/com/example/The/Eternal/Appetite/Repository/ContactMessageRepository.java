package com.example.The.Eternal.Appetite.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.The.Eternal.Appetite.entity.ContactMessage;

	public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
	    // view all contact messages
	}


