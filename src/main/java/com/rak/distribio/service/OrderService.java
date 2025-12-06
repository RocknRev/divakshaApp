package com.rak.distribio.service;

import com.rak.distribio.entity.Order;
import com.rak.distribio.entity.Product;
import com.rak.distribio.entity.Sale;
import com.rak.distribio.repository.OrderRepository;
import com.rak.distribio.repository.ProductRepository;
import com.rak.distribio.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

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
	public Order createOrder(Long productId, Long buyerUserId, Long sellerUserId, String paymentProofUrl, java.math.BigDecimal amount, String deliveryName, String deliveryPhone, String deliveryAddress, Long quantity) {
		logger.info("Creating order: productId={}, buyerId={}, sellerId={}, paymentProofUrl={}, amount={}",
			productId, buyerUserId, sellerUserId, paymentProofUrl, amount);

		if(productId==null) throw new IllegalArgumentException("Invalid Product Id");
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

		Order order = new Order(productId, buyerUserId, sellerUserId, paymentProofUrl, amount, "PENDING", deliveryName, deliveryPhone, deliveryAddress, quantity);
		order = orderRepository.save(order);

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
			Sale sale = new Sale(order.getSellerUserId(), order.getBuyerUserId(), order.getAmount());
			sale = saleRepository.save(sale);

			userService.updateLastSaleAt(order.getSellerUserId());
			referralService.calculateAndDistributeCommissions(sale);
			logger.info("Order approved & commissions calculated.");
		}else{
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

