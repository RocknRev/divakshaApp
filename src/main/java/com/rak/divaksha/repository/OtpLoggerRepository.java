package com.rak.divaksha.repository;

import com.rak.divaksha.entity.OtpLogger;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpLoggerRepository extends JpaRepository<OtpLogger, Long> {

    Optional<OtpLogger> findTopByEmailOrderByIdDesc(String email);

}

