package com.rak.divaksha.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rak.divaksha.entity.ContactQuery;

public interface ContactQueryRepository extends JpaRepository<ContactQuery, Long> {
}
