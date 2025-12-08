package com.rak.divaksha.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CreateCartOrderRequest {

    private Long buyerId;

    @NotEmpty
    private List<CreateCartOrderItemRequest> items;

    @NotNull
    private BigDecimal totalAmount;

    @NotBlank
    private String paymentProofUrl;

    @NotBlank
    @Size(min = 5, max = 500)
    private String deliveryAddress;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String deliveryPhone;

    @NotBlank
    @Size(min = 2, max = 100)
    private String deliveryName;

    @NotBlank
    @Email
    private String deliveryEmail;

    private String affiliateCode;    

    public CreateCartOrderRequest() {
    }

    public CreateCartOrderRequest(Long buyerId, List<CreateCartOrderItemRequest> items, BigDecimal totalAmount, String paymentProofUrl, String deliveryAddress, String deliveryPhone, String deliveryName, String deliveryEmail, String affiliateCode) {
        this.buyerId = buyerId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentProofUrl = paymentProofUrl;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.deliveryName = deliveryName;
        this.deliveryEmail = deliveryEmail;
        this.affiliateCode = affiliateCode;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }
    
    public List<CreateCartOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateCartOrderItemRequest> items) {
        this.items = items;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getPaymentProofUrl() {
        return paymentProofUrl;
    }
    
    public void setPaymentProofUrl(String paymentProofUrl) {
        this.paymentProofUrl = paymentProofUrl;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }
    
    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryEmail() {
        return deliveryEmail;
    }

    public void setDeliveryEmail(String deliveryEmail) {
        this.deliveryEmail = deliveryEmail;
    }

    public String getAffiliateCode() {
        return affiliateCode;
    }

    public void setAffiliateCode(String affiliateCode) {
        this.affiliateCode = affiliateCode;
    }

}