package com.rak.divaksha.controller;

import com.rak.divaksha.dto.ProductRequest;
import com.rak.divaksha.dto.ProductResponse;
import com.rak.divaksha.entity.Product;
import com.rak.divaksha.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(required = false) Boolean available) {
		List<Product> products;
		if (available != null && available) {
			products = productService.getAvailableProducts();
		} else {
			products = productService.getAllProducts();
		}

		List<ProductResponse> responses = products.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return productService.getProductById(id)
				.map(product -> ResponseEntity.ok(mapToResponse(product)))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		try {
			Product product = productService.createProduct(
					request.getSku(),
					request.getName(),
					request.getDescription(),
					request.getPrice(),
					request.getImageUrl(),
					request.getStock()
			);
			return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(product));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		try {
			Product product = productService.updateProduct(
					id,
					request.getSku(),
					request.getName(),
					request.getDescription(),
					request.getPrice(),
					request.getImageUrl(),
					request.getAvailable(),
					request.getStock()
			);
			return ResponseEntity.ok(mapToResponse(product));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		try {
			productService.deleteProduct(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private ProductResponse mapToResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setProductId(product.getId());
		response.setSku(product.getSku());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setPrice(product.getPrice());
		response.setImageUrl(product.getImageUrl());
		response.setAvailable(product.getAvailable());
		response.setCreatedAt(product.getCreatedAt());
		response.setStock(product.getStock());
		return response;
	}
}

