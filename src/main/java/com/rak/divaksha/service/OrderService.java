package com.rak.divaksha.service;

import com.rak.divaksha.entity.Order;
import com.rak.divaksha.entity.Product;
import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.repository.OrderRepository;
import com.rak.divaksha.repository.ProductRepository;
import com.rak.divaksha.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private MailService mailService;

	private final OrderRepository orderRepository;
	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;
	private final UserService userService;
	private final ReferralService referralService;

	public OrderService(OrderRepository orderRepository, SaleRepository saleRepository,
	                    ProductRepository productRepository, UserService userService, ReferralService referralService) {
		this.orderRepository = orderRepository;
		this.saleRepository = saleRepository;
		this.productRepository = productRepository;
		this.userService = userService;
		this.referralService = referralService;
	}

	@Transactional
	public Order createOrder(Long productId, Long buyerUserId, Long sellerUserId, String paymentProofUrl, java.math.BigDecimal amount, String deliveryName, String deliveryPhone, String deliveryAddress, Long quantity, String email) {
		logger.info("Creating order: productId={}, buyerId={}, sellerId={}, paymentProofUrl={}, amount={}",
			productId, buyerUserId, sellerUserId, paymentProofUrl, amount);

		if(productId==null) throw new IllegalArgumentException("Invalid Product Id");
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

		Order order = new Order(productId, buyerUserId, sellerUserId, paymentProofUrl, amount, "PENDING", deliveryName, deliveryPhone, deliveryAddress, quantity, email);
		order = orderRepository.save(order);

		mailService.sendOrderConfirmation(email, order.getOrderId(), product.getName(), amount.toString());
		logger.info("Order created: ID={}", order.getOrderId());
		return order;
	}

	@Transactional
	public void verifyAndApprovePayment(Long orderId, String status) {
		if(orderId==null) throw new IllegalArgumentException("Invalid Order Id");
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found"));

		order.setStatus(status);
		orderRepository.save(order);

		if(status.equalsIgnoreCase("PAID")){
			if(order.getSellerUserId()==null) order.setSellerUserId(1L);
			Sale sale = new Sale(order.getSellerUserId(), order.getBuyerUserId(), order.getAmount());
			sale = saleRepository.save(sale);

			userService.updateLastSaleAt(order.getSellerUserId());
			referralService.calculateAndDistributeCommissions(sale);
			mailService.sendOrderPaidMail(order.getEmail(), order.getOrderId());	
			logger.info("Order approved & commissions calculated.");
		}else if(status.equalsIgnoreCase("REJECTED")){
			mailService.sendOrderRejectedMail(order.getEmail(), order.getOrderId());
			logger.info("Order Rejected successfully.");
		}
	}


	public Page<Order> getAllOrders(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
		return orderRepository.findByStatus(status, pageable);
	}

	public List<Order> getOrdersByBuyer(Long buyerUserId) {
		return orderRepository.findByBuyerUserId(buyerUserId);
	}

	public List<Order> getOrdersBySeller(Long sellerUserId) {
		return orderRepository.findBySellerUserId(sellerUserId);
	}
}

