package com.simpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponse {
    private UUID paymentId;
    private UUID transaccionId;
    private String paymentMethod;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String paymentUrl;
    private String confirmationCode;
    private String qrCode;
    private String whatsappMessage;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    private String errorMessage;
    
    // Constructors
    public PaymentResponse() {}
    
    public PaymentResponse(UUID paymentId, UUID transaccionId, String paymentMethod, 
                          BigDecimal amount, String status, String paymentUrl) {
        this.paymentId = paymentId;
        this.transaccionId = transaccionId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.paymentUrl = paymentUrl;
        this.currency = "COP";
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getPaymentId() { return paymentId; }
    public void setPaymentId(UUID paymentId) { this.paymentId = paymentId; }
    
    public UUID getTransaccionId() { return transaccionId; }
    public void setTransaccionId(UUID transaccionId) { this.transaccionId = transaccionId; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPaymentUrl() { return paymentUrl; }
    public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }
    
    public String getConfirmationCode() { return confirmationCode; }
    public void setConfirmationCode(String confirmationCode) { this.confirmationCode = confirmationCode; }
    
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    
    public String getWhatsappMessage() { return whatsappMessage; }
    public void setWhatsappMessage(String whatsappMessage) { this.whatsappMessage = whatsappMessage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}