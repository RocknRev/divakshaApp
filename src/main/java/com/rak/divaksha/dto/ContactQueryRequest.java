package com.rak.divaksha.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactQueryRequest {

    @NotBlank
    @Size(min = 2)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 3)
    private String subject;

    @NotBlank
    @Size(min = 10)
    private String message;

    public ContactQueryRequest() {
    }

    public ContactQueryRequest(String name, String email, String subject, String message) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
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
}
