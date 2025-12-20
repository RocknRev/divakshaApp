package com.rak.divaksha.service;

import com.rak.divaksha.dto.ContactQueryRequest;
import com.rak.divaksha.dto.ContactQueryResponse;

public interface ContactQueryService {

    public ContactQueryResponse saveQuery(ContactQueryRequest request, Long userId);
}
