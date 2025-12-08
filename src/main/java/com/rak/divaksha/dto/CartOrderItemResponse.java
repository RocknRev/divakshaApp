package com.rak.divaksha.dto;

import java.math.BigDecimal;

public class CartOrderItemResponse {

    private Long productId;
    private int quantity;
    private BigDecimal price;
    private Long orderId;

    public CartOrderItemResponse(Long productId, int quantity, BigDecimal price, Long orderId) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
