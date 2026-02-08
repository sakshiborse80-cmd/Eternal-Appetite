package com.example.The.Eternal.Appetite.Controller;



	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
	import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.The.Eternal.Appetite.Repository.ContactMessageRepository;
import com.example.The.Eternal.Appetite.entity.ContactMessage;

	@Controller
	public class AboutContactController {

	    @GetMapping("/about")
	    public String showAboutPage() {
	        return "about"; // Loads templates/about.html
	    }

	    @GetMapping("/contact")
	    public String showContactPage() {
	        return "contact"; // Loads templates/contact.html
	    }
	    
	    @Autowired
	    private ContactMessageRepository contactMessageRepository;

	    @PostMapping("/contact/submit")
	    public String handleContactForm(@RequestParam String name,
	                                    @RequestParam String email,
	                                    @RequestParam String message) {
	        ContactMessage contactMessage = new ContactMessage();
	        contactMessage.setName(name);
	        contactMessage.setEmail(email);
	        contactMessage.setMessage(message);

	        contactMessageRepository.save(contactMessage);

	        return "thank-you"; // ye template tu banale
	    }
	}

