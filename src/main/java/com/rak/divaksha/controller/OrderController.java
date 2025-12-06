package com.rak.divaksha.controller;

import com.rak.divaksha.dto.OrderRequest;
import com.rak.divaksha.dto.OrderResponse;
import com.rak.divaksha.dto.PagedResponse;
import com.rak.divaksha.entity.Order;
import com.rak.divaksha.service.OrderService;
import jakarta.validation.Valid;

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

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
		try {
			Order order = orderService.createOrder(
				request.getProductId(),
				request.getBuyerId(),
				request.getSellerId(),
				request.getPaymentProofUrl(),
				request.getAmount(),
				request.getDeliveryName(), request.getDeliveryPhone(), request.getDeliveryAddress(), request.getQuantity(), request.getDeliveryEmail()
			);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(order));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping
	public ResponseEntity<PagedResponse<Order>> getAllOrders(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String status) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Order> orders=null;
		if(status!=null && !status.isEmpty() && !status.isBlank())
			orders = orderService.getOrdersByStatus(status, pageable);
		else
			orders = orderService.getAllOrders(pageable);
		// List<OrderResponse> responses = orders.stream()
		// 	.map(this::mapToResponse)
		// 	.collect(Collectors.toList());
		// return ResponseEntity.ok(responses);
		return ResponseEntity.ok(new PagedResponse<>(orders));
	}

	private OrderResponse mapToResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setOrderId(order.getOrderId());
		response.setProductId(order.getProductId());
		response.setBuyerId(order.getBuyerUserId());
		response.setSellerId(order.getSellerUserId());
		response.setPaymentProofUrl(order.getPaymentProofUrl());
		response.setAmount(order.getAmount());
		response.setStatus(order.getStatus());
		response.setCreatedAt(order.getCreatedAt());
		response.setDeliveryName(order.getDeliveryName());
		response.setDeliveryPhone(order.getDeliveryPhone());
		response.setDeliveryAddress(order.getDeliveryAddress());
		return response;
	}

	@PostMapping("/statusUpdate")
	private ResponseEntity<String> updatePaymentStatus(@RequestBody OrderRequest request){
		orderService.verifyAndApprovePayment(request.getOrderId(), request.getStatus());
		return ResponseEntity.ok("Payment status updated Successfully.");
	}

}

