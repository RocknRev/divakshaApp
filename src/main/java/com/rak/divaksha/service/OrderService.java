package com.rak.divaksha.service;

import com.rak.divaksha.dto.CartOrderResponse;
import com.rak.divaksha.dto.CreateCartOrderRequest;
import com.rak.divaksha.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {

    public CartOrderResponse submitOrder(CreateCartOrderRequest req);
    public void verifyAndApprovePayment(Long orderId, String status);
    public List<Order> getOrdersByBuyer(Long buyerId);
    public Page<Order> getAllOrders(Pageable pageable);
    public Page<Order> getOrdersByStatus(String status, Pageable pageable);
}
