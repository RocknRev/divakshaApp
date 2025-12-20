package com.rak.divaksha.service;

import com.rak.divaksha.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
	public List<Product> getAllProducts();
	public List<Product> getAvailableProducts();
	public Optional<Product> getProductById(Long id);
	public Product createProduct(String sku, String name, String description, BigDecimal price, String imageUrl, Long stock);
	public Product updateProduct(Long id, String sku, String name, String description, BigDecimal price, String imageUrl, Boolean available, Long stock);
	public void deleteProduct(Long id);
}

