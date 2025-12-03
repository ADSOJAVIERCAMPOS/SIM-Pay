package com.simpay.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device_logs")
public class DeviceLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String provider; // google, facebook, email
    
    @Column(nullable = false)
    private String userAgent;
    
    @Column(nullable = false)
    private String platform;
    
    private String language;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private String userEmail;
    
    private String verificationCode;
    
    private Boolean verified = false;
    
    private LocalDateTime verifiedAt;
    
    @Column(columnDefinition = "TEXT")
    private String notificationSentTo; // email superadmin
    
    // Constructors
    public DeviceLog() {}
    
    public DeviceLog(String provider, String userAgent, String platform, String language) {
        this.provider = provider;
        this.userAgent = userAgent;
        this.platform = platform;
        this.language = language;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public void setProvider(String provider) {
        this.provider = provider;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getVerificationCode() {
        return verificationCode;
    }
    
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
    
    public Boolean getVerified() {
        return verified;
    }
    
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    
    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }
    
    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
    
    public String getNotificationSentTo() {
        return notificationSentTo;
    }
    
    public void setNotificationSentTo(String notificationSentTo) {
        this.notificationSentTo = notificationSentTo;
    }
}
