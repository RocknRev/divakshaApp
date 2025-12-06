package com.rak.distribio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrderRequest {

	@NotNull(message = "Product ID is required")
	private Long productId;

	private Long buyerId;

	private Long sellerId;

	@NotBlank(message = "Payment Proof URL is required")
	private String paymentProofUrl;

	@NotNull(message = "Amount is required")
	@Positive(message = "Amount must be positive")
	private BigDecimal amount;

	private String status;
	private Long orderId;

	@NotBlank(message = "Delivery name is required")
	private String deliveryName;

	@NotBlank(message = "Delivery phone number is required")
	private String deliveryPhone;

	@NotBlank(message = "Delivery address is required")
	private String deliveryAddress;

	private Long quantity;


	public OrderRequest() {
	}

	public OrderRequest(Long productId, Long buyerId, Long sellerId, String paymentProofUrl, BigDecimal amount, String status, Long orderId, String deliveryName, String deliveryPhone, String deliveryAddress, Long quantity) {
		this.productId = productId;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.paymentProofUrl = paymentProofUrl;
		this.amount = amount;
		this.status = status;
		this.orderId = orderId;
		this.deliveryName = deliveryName;
		this.deliveryPhone = deliveryPhone;
		this.deliveryAddress = deliveryAddress;
		this.quantity = quantity;
	}

	// Getters and Setters
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getPaymentProofUrl() {
		return paymentProofUrl;
	}

	public void setPaymentProofUrl(String paymentProofUrl) {
		this.paymentProofUrl = paymentProofUrl;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryPhone() {
		return deliveryPhone;
	}

	public void setDeliveryPhone(String deliveryPhone) {
		this.deliveryPhone = deliveryPhone;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}

