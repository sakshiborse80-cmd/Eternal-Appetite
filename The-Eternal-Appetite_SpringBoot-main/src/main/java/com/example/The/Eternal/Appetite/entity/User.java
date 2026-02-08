package com.example.The.Eternal.Appetite.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;       // full name
    private String email;
    
    private String password;
    private String address;
    private String mobile;
    private String role;       // "user" or "admin"
    private String otp;
}
