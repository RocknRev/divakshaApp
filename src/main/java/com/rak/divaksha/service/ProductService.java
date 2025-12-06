package com.rak.divaksha.service;

import com.rak.divaksha.entity.Product;
import com.rak.divaksha.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<Product> getAvailableProducts() {
		return productRepository.findByAvailableTrue();
	}

	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	@Transactional
	public Product createProduct(String sku, String name, String description, BigDecimal price, String imageUrl) {
		logger.info("Creating product: {}", name);

		Product product = new Product(sku, name, description, price, imageUrl);
		return productRepository.save(product);
	}

	@Transactional
	public Product updateProduct(Long id, String sku, String name, String description, BigDecimal price, String imageUrl, Boolean available) {
		logger.info("Updating product: {}", id);

		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			throw new IllegalArgumentException("Product not found: " + id);
		}

		Product product = productOpt.get();
		if (sku != null) product.setSku(sku);
		if (name != null) product.setName(name);
		if (description != null) product.setDescription(description);
		if (price != null) product.setPrice(price);
		if (imageUrl != null) product.setImageUrl(imageUrl);
		if (available != null) product.setAvailable(available);

		return productRepository.save(product);
	}

	@Transactional
	public void deleteProduct(Long id) {
		logger.info("Deleting product: {}", id);
		if (!productRepository.existsById(id)) {
			throw new IllegalArgumentException("Product not found: " + id);
		}
		productRepository.deleteById(id);
	}
}

