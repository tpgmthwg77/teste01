package com.bluegestao.whatsappbot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    
    private String name;
    private LocalDateTime firstContact;
    private LocalDateTime lastContact;
    private Integer messageCount = 0;
    
    // Constructors
    public Contact() {}
    
    public Contact(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.firstContact = LocalDateTime.now();
        this.lastContact = LocalDateTime.now();
        this.messageCount = 1;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDateTime getFirstContact() { return firstContact; }
    public void setFirstContact(LocalDateTime firstContact) { this.firstContact = firstContact; }
    
    public LocalDateTime getLastContact() { return lastContact; }
    public void setLastContact(LocalDateTime lastContact) { this.lastContact = lastContact; }
    
    public Integer getMessageCount() { return messageCount; }
    public void setMessageCount(Integer messageCount) { this.messageCount = messageCount; }
    
    public void incrementMessageCount() {
        this.messageCount++;
        this.lastContact = LocalDateTime.now();
    }
}