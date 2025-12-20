package com.rak.divaksha.service.impl;

import com.rak.divaksha.entity.Product;
import com.rak.divaksha.repository.ProductRepository;
import com.rak.divaksha.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getAvailableProducts() {
		return productRepository.findByAvailableTrue();
	}

	@Override
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	@Transactional
	@Override
	public Product createProduct(String sku, String name, String description, BigDecimal price, String imageUrl, Long stock) {
		logger.info("Creating product: {}", name);

		Product product = new Product(sku, name, description, price, imageUrl, stock);
		return productRepository.save(product);
	}

	@Transactional
	@Override
	public Product updateProduct(Long id, String sku, String name, String description, BigDecimal price, String imageUrl, Boolean available, Long stock) {
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
		if (stock != null) product.setStock(stock);

		return productRepository.save(product);
	}

	@Transactional
	@Override
	public void deleteProduct(Long id) {
		logger.info("Deleting product: {}", id);
		if (!productRepository.existsById(id)) {
			throw new IllegalArgumentException("Product not found: " + id);
		}
		productRepository.deleteById(id);
	}
}

