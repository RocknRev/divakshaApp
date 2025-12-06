package com.rak.distribio.repository;

import com.rak.distribio.entity.Sale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	Page<Sale> findBySellerUserId(Long sellerUserId, Pageable pageable);

	List<Sale> findByBuyerId(Long buyerId);
}

