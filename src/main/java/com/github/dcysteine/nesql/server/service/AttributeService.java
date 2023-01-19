package com.github.dcysteine.nesql.server.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing common attributes to Thymeleaf templates. */
@Service
public class AttributeService {
    @Autowired
    HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }
}
