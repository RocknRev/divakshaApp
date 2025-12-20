package com.rak.divaksha.service.impl;

import org.springframework.stereotype.Service;

import com.rak.divaksha.dto.ContactQueryRequest;
import com.rak.divaksha.dto.ContactQueryResponse;
import com.rak.divaksha.entity.ContactQuery;
import com.rak.divaksha.repository.ContactQueryRepository;
import com.rak.divaksha.service.ContactQueryService;

@Service
public class ContactQueryServiceImpl implements ContactQueryService {

    private final ContactQueryRepository repository;

    public ContactQueryServiceImpl(ContactQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContactQueryResponse saveQuery(ContactQueryRequest request, Long userId) {

        ContactQuery query = new ContactQuery();
        query.setName(request.getName());
        query.setEmail(request.getEmail());
        query.setSubject(request.getSubject());
        query.setMessage(request.getMessage());
        query.setUserId(userId);

        ContactQuery saved = repository.save(query);

        return new ContactQueryResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getSubject(),
                saved.getMessage(),
                saved.getCreatedAt().toString(),
                saved.getStatus().name()
        );
    }
}
