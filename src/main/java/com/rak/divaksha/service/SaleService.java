package com.rak.divaksha.service;

import com.rak.divaksha.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

public interface SaleService {

	public Sale createSale(Long sellerId, Long buyerId, BigDecimal totalAmount) ;

	public Sale createSale(Long sellerId, Long buyerId, Long affiliateUserId, BigDecimal totalAmount);

	public Page<Sale> getAllSales(Pageable pageable);

	public Page<Sale> getSalesBySeller(Long sellerUserId, Pageable pageable) ;
}

