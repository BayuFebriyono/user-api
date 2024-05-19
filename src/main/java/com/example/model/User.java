package com.example.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(length = 255, unique = true)
    public String email;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 6, message = "Username must be at least 6 characters long")
    @Column(length = 100, unique = true)
    public String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{6,}$",
            message = "Password must be at least 6 characters long and contain at least one uppercase letter, one lowercase letter, and one special character")
    @Column(length = 255)
    public String password;

    @Column(nullable = false)
    public String role;

    public LocalDateTime lastLogin;

    public int loginAttempt;

    @Column(nullable = false, updatable = false)
    public LocalDateTime dateCreated;

    @Column(nullable = false)
    public LocalDateTime dateUpdated;



    @PrePersist
    protected void onCreate() {
        dateCreated = LocalDateTime.now();
        dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = LocalDateTime.now();
    }
}
