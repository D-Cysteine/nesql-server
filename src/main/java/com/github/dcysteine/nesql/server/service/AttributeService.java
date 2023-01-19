package com.github.dcysteine.nesql.server.service;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** Service providing common attributes to Thymeleaf templates. */
@Service
public class AttributeService {
    @Autowired
    HttpServletRequest request;

    public ImmutableList<Table> getNavBarTables() {
        return Table.TABLES.get(Plugin.BASE);
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
