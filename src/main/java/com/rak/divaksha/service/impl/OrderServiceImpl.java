package com.rak.divaksha.service.impl;

import com.rak.divaksha.dto.CartOrderItemResponse;
import com.rak.divaksha.dto.CartOrderResponse;
import com.rak.divaksha.dto.CreateCartOrderItemRequest;
import com.rak.divaksha.dto.CreateCartOrderRequest;
import com.rak.divaksha.entity.Order;
import com.rak.divaksha.entity.OrderItem;
import com.rak.divaksha.entity.Product;
import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.entity.User;
import com.rak.divaksha.repository.OrderItemRepository;
import com.rak.divaksha.repository.OrderRepository;
import com.rak.divaksha.repository.ProductRepository;
import com.rak.divaksha.repository.SaleRepository;
import com.rak.divaksha.repository.UserRepository;
import com.rak.divaksha.service.OrderService;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SaleRepository saleRepository;
    private final UserServiceImpl userService;
    private final ReferralServiceImpl referralService;
    private final MailServiceImpl mailService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            SaleRepository saleRepository,
            UserServiceImpl userService,
            ReferralServiceImpl referralService,
            MailServiceImpl mailService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
        this.userService = userService;
        this.referralService = referralService;
        this.mailService = mailService;
    }

    @Transactional
    @Override
    public CartOrderResponse submitOrder(CreateCartOrderRequest req) {

        log.info("Submitting order for buyer={}, itemCount={}", req.getBuyerId(), req.getItems().size());

        Long sellerUserId = resolveSellerFromAffiliate(req.getAffiliateCode(), req.getBuyerId());

        BigDecimal computedTotal =
                req.getItems().stream()
                        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (computedTotal.compareTo(req.getTotalAmount()) != 0) {
            throw new IllegalArgumentException("Total amount mismatch");
        }

        long totalQty = 0L;
        for (CreateCartOrderItemRequest item : req.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

            if (product.getStock() != null && product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            totalQty += item.getQuantity();
        }

        Order order = new Order();
        order.setBuyerUserId(req.getBuyerId());
        order.setSellerUserId(sellerUserId);
        order.setAmount(req.getTotalAmount());
        order.setPaymentProofUrl(req.getPaymentProofUrl());
        order.setDeliveryAddress(req.getDeliveryAddress());
        order.setDeliveryPhone(req.getDeliveryPhone());
        order.setDeliveryName(req.getDeliveryName());
        order.setEmail(req.getDeliveryEmail());
        order.setStatus("PENDING");
        order.setQuantity(totalQty);
        order.setCreatedAt(OffsetDateTime.now());

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateCartOrderItemRequest itemReq : req.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(itemReq.getPrice());
            item.setSellerId(itemReq.getSellerId());

            orderItems.add(item);

            Product p = productRepository.findById(itemReq.getProductId()).orElseThrow();
            if (p.getStock() != null) {
                p.setStock(p.getStock() - itemReq.getQuantity());
                productRepository.save(p);
            }
        }

        orderItemRepository.saveAll(orderItems);

        mailService.sendOrderConfirmation(
                order.getEmail(),
                savedOrder.getOrderId(),
                order.getAmount().toString()
        );

        List<CartOrderItemResponse> respItems = orderItems.stream()
                .map(i -> new CartOrderItemResponse(
                        i.getProductId(),
                        i.getQuantity(),
                        i.getPrice(),
                        savedOrder.getOrderId()
                ))
                .toList();

        return new CartOrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getBuyerUserId(),
                savedOrder.getAmount(),
                savedOrder.getStatus(),
                respItems,
                savedOrder.getDeliveryAddress(),
                savedOrder.getDeliveryPhone(),
                savedOrder.getDeliveryName(),
                savedOrder.getEmail(),
                savedOrder.getCreatedAt().toString()
        );
    }

    private Long resolveSellerFromAffiliate(String affiliateCode, Long buyerId) {
        if (affiliateCode == null || affiliateCode.isBlank()) {
			if(buyerId!=null)  return buyerId; else return 1L;
		}
        Optional<User> affiliate = userRepository.findByAffiliateCode(affiliateCode);
                // .orElseThrow(() -> new IllegalArgumentException("Affiliate user not found: " + affiliateCode));
		if(affiliate==null || !affiliate.isPresent()){
			log.info("Affiliate user not found: " + affiliateCode);
			return 1L;
		} 
        return affiliate.get().getId();
    }


    @Transactional
    @Override
    public void verifyAndApprovePayment(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        if (status.equalsIgnoreCase("PAID")) {
            List<OrderItem> items = orderItemRepository.findByOrder_OrderId(orderId);

            for (OrderItem item : items) {
                Long sellerId = item.getSellerId() != null ? item.getSellerId() : order.getSellerUserId();
                BigDecimal saleAmount = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                Sale sale = new Sale(sellerId, order.getBuyerUserId(), saleAmount);
                sale = saleRepository.save(sale);

                userService.updateLastSaleAt(sellerId);
                referralService.calculateAndDistributeCommissions(sale);
            }

            mailService.sendOrderPaidMail(order.getEmail(), order.getOrderId());
            log.info("Order {} approved with {} items", orderId, items.size());

        } else if (status.equalsIgnoreCase("REJECTED")) {
            mailService.sendOrderRejectedMail(order.getEmail(), order.getOrderId());
        }
    }

    @Override
    public List<Order> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findByBuyerUserId(buyerId);
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> getOrdersByStatus(String status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }
}
