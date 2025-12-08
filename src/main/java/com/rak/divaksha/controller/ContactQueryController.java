package com.rak.divaksha.controller;

import com.rak.divaksha.dto.ContactQueryRequest;
import com.rak.divaksha.dto.ContactQueryResponse;
import com.rak.divaksha.service.ContactQueryService;
import com.rak.divaksha.util.JwtUtil;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/contact")
public class ContactQueryController {

    private final ContactQueryService service;
    private final JwtUtil jwtUtil;

    public ContactQueryController(ContactQueryService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/query")
    public ResponseEntity<ContactQueryResponse> submitQuery(
            @Valid @RequestBody ContactQueryRequest request,
            HttpServletRequest httpRequest) {

        Long userId = null;

        try {
            String token = jwtUtil.extractToken(httpRequest);
            userId = jwtUtil.extractUserId(token);
        } catch (Exception ignored) {}

        ContactQueryResponse response = service.saveQuery(request, userId);

        return ResponseEntity.ok(response);
    }
}
