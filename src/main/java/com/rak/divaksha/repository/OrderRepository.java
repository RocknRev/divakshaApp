package com.rak.divaksha.repository;

import com.rak.divaksha.entity.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByBuyerUserId(Long buyerUserId);

	List<Order> findBySellerUserId(Long sellerUserId);

	Page<Order> findByStatus(String status, Pageable pageable);
}

