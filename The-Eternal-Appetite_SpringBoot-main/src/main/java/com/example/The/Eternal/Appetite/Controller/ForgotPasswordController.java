package com.example.The.Eternal.Appetite.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.The.Eternal.Appetite.Repository.UserRepository;
import com.example.The.Eternal.Appetite.Service.EmailService;
import com.example.The.Eternal.Appetite.entity.User;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	    private JavaMailSender mailSender;
	 @Autowired
	 private UserRepository userRepository;
	 @Autowired
	 private EmailService emailService;


	    private Map<String, String> otpStorage = new HashMap<>();

	    @GetMapping("/forgot-password")
	    public String forgotPage() {
	        return "forgot-password";
	    }
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email, Model model) {
        String otp = String.valueOf((int) (Math.random() * 900000 + 100000));
        otpStorage.put(email, otp);

//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Your OTP for Reset");
//        message.setText("Your OTP is: " + otp);
//        mailSender.send(message);
        
        // ✅ Use your service instead:
        emailService.sendOtpEmail(email, otp);
        model.addAttribute("email", email);
        return "verify-otp";
    }


    
    @PostMapping("/verify-otp")
    public String verify(@RequestParam String email,
                            @RequestParam String otp,
                            @RequestParam String newPassword,
                            Model model) {
    	model.addAttribute("user", new User()); 
       User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Invalid Email");
            return "verify-otp";
        }

        // Check if OTP matches and not expired
        if (otp.equals(otpStorage.get(email))) {

	
	
    // ✅ Update password (hashing is recommended)
	user.setPassword(newPassword);
//    user.setPassword(newPassword); // OR encode: passwordEncoder.encode(newPassword)

    // Clear OTP fields
  user.setOtp(null);
    

    // Save updated user
    userRepository.save(user);

    model.addAttribute("message", "Password updated successfully!");
    return "login";
} else {
    model.addAttribute("error", "Invalid or expired OTP");
    model.addAttribute("email", email);
    return "verify-otp";
}
}
}