package com.rak.divaksha.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartOrderResponse {

    private Long orderId;
    private Long buyerId;
    private BigDecimal totalAmount;
    private String status;
    private List<CartOrderItemResponse> items;
    private String deliveryAddress;
    private String deliveryPhone;
    private String deliveryName;
    private String deliveryEmail;
    private String createdAt;

    public CartOrderResponse(Long orderId, Long buyerId, BigDecimal totalAmount, String status,
                             List<CartOrderItemResponse> items,
                             String deliveryAddress, String deliveryPhone,
                             String deliveryName, String deliveryEmail,
                             String createdAt) {

        this.orderId = orderId;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPhone = deliveryPhone;
        this.deliveryName = deliveryName;
        this.deliveryEmail = deliveryEmail;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartOrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartOrderItemResponse> items) {
        this.items = items;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    
}
