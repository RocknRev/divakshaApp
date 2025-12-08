package com.rak.divaksha.controller;

import com.rak.divaksha.dto.CartOrderResponse;
import com.rak.divaksha.dto.CreateCartOrderRequest;
import com.rak.divaksha.dto.PagedResponse;
import com.rak.divaksha.entity.Order;
import com.rak.divaksha.service.OrderService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/submit")
	public ResponseEntity<CartOrderResponse> submitOrder(
			@Valid @RequestBody CreateCartOrderRequest request,
			@RequestHeader(name = "Authorization", required = true) String authHeader
	) {
		try {
			CartOrderResponse response = orderService.submitOrder(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException ex) {
			log.error("Validation error submitting order", ex);
			return ResponseEntity.badRequest().build();
		} catch (Exception ex) {
			log.error("Internal error submitting order", ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/statusUpdate")
	public ResponseEntity<String> updatePaymentStatus(
			@RequestParam Long orderId,
			@RequestParam String status
	) {
		orderService.verifyAndApprovePayment(orderId, status);
		return ResponseEntity.ok("Payment status updated successfully.");
	}

	// ------------------------------------------------------------------------------
	//  Pagination Support for Admin Panel
	// ------------------------------------------------------------------------------
	@GetMapping
	public ResponseEntity<PagedResponse<Order>> getAllOrders(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam(required = false) String status
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Order> orders = (status != null && !status.isBlank())
				? orderService.getOrdersByStatus(status, pageable)
				: orderService.getAllOrders(pageable);

		return ResponseEntity.ok(new PagedResponse<>(orders));
	}
}
