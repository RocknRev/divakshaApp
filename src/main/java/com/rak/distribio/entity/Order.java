package com.rak.distribio.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "buyer_user_id")
	private Long buyerUserId;

	@Column(name = "seller_user_id")
	private Long sellerUserId;

	@Column(name = "payment_proof_url", length = 255)
	private String paymentProofUrl;

	@Column(precision = 18, scale = 2)
	private BigDecimal amount;

	@Column(length = 50)
	private String status;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "delivery_name")
	private String deliveryName;
	@Column(name = "delivery_phone")
	private String deliveryPhone;
	@Column(name = "delivery_address")
	private String deliveryAddress;

	@Column(name = "quantity")
	private Long quantity;

	// Constructors
	public Order() {
	}

	public Order(Long productId, Long buyerUserId, Long sellerUserId, String paymentProofUrl, BigDecimal amount, String status, String deliveryName, String deliveryPhone, String deliveryAddress, Long quantity) {
		this.productId = productId;
		this.buyerUserId = buyerUserId;
		this.sellerUserId = sellerUserId;
		this.paymentProofUrl = paymentProofUrl;
		this.amount = amount;
		this.status = status;
		this.deliveryName = deliveryName;
		this.deliveryPhone = deliveryPhone;
		this.deliveryAddress = deliveryAddress;
		this.quantity = quantity;
	}

	// Getters and Setters
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getBuyerUserId() {
		return buyerUserId;
	}

	public void setBuyerUserId(Long buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	public Long getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Long sellerUserId) {
		this.sellerUserId = sellerUserId;
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

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
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

