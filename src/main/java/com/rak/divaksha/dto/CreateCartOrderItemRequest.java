package com.rak.divaksha.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateCartOrderItemRequest {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    @NotNull
    private BigDecimal price;

    private Long sellerId;

    public CreateCartOrderItemRequest() {
    }

    public CreateCartOrderItemRequest(Long productId, int quantity, BigDecimal price, Long sellerId) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.sellerId = sellerId;
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

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    
}