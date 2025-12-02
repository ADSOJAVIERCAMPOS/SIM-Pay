package com.simpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentRequest {
    private UUID transaccionId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String returnUrl;
    private String cancelUrl;
    
    // Constructors
    public PaymentRequest() {}
    
    public PaymentRequest(UUID transaccionId, BigDecimal amount, String currency, 
                         String description, String customerName, String customerPhone) {
        this.transaccionId = transaccionId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }
    
    // Getters and Setters
    public UUID getTransaccionId() { return transaccionId; }
    public void setTransaccionId(UUID transaccionId) { this.transaccionId = transaccionId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
    
    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }
}