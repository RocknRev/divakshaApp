package com.rak.divaksha.repository;

import com.rak.divaksha.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findBySku(String sku);

	List<Product> findByAvailableTrue();
}

