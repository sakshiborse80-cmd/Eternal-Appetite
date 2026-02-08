package com.example.The.Eternal.Appetite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.The.Eternal.Appetite.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    User findByEmail(String email); 
}

