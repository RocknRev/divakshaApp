package com.rak.divaksha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rak.divaksha.entity.OrderItem;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_OrderId(Long orderId);
}
