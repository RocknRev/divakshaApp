package com.rak.divaksha.entity;


import java.time.LocalDateTime;

import com.rak.divaksha.util.QueryStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_queries")
public class ContactQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private QueryStatus status;

    // Optional: track user if logged in
    private Long userId;

    public ContactQuery() {
        this.createdAt = LocalDateTime.now();
        this.status = QueryStatus.PENDING;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public QueryStatus getStatus() {
        return status;
    }
    public void setStatus(QueryStatus status) {
        this.status = status;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}