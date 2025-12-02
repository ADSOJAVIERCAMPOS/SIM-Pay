package com.simpay.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class WhatsAppPaymentRequest {
    private UUID transaccionId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String customerName;
    private String customerPhone;
    private String paymentMethod; // "NEQUI" o "DAVIPLATA"
    private String message;
    
    // Constructors
    public WhatsAppPaymentRequest() {}
    
    public WhatsAppPaymentRequest(UUID transaccionId, BigDecimal amount, String customerPhone, 
                                 String paymentMethod, String description) {
        this.transaccionId = transaccionId;
        this.amount = amount;
        this.customerPhone = customerPhone;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.currency = "COP";
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
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}