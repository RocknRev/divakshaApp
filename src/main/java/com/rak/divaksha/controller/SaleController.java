package com.rak.divaksha.controller;

import com.rak.divaksha.dto.PagedResponse;
import com.rak.divaksha.entity.Sale;
import com.rak.divaksha.service.SaleService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

	private final SaleService saleService;

	public SaleController(SaleService saleService) {
		this.saleService = saleService;
	}

	@GetMapping
	public ResponseEntity<PagedResponse<Sale>> getSales(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam(required = false) Long sellerId) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Sale> sales;

		if (sellerId != null)
			sales = saleService.getSalesBySeller(sellerId, pageable);
		else
			sales = saleService.getAllSales(pageable);
		
		return ResponseEntity.ok(new PagedResponse<>(sales));
	}


		// private SaleResponse mapToResponse(Sale sale) {
		// 	SaleResponse response = new SaleResponse();
		// 	response.setId(sale.getId());
		// 	response.setSellerUserId(sale.getSellerUserId());
		// 	response.setBuyerId(sale.getBuyerId());
		// 	response.setAffiliateUserId(sale.getAffiliateUserId());
		// 	response.setTotalAmount(sale.getTotalAmount());
		// 	response.setCreatedAt(sale.getCreatedAt());
		// 	return response;
		// }
}

