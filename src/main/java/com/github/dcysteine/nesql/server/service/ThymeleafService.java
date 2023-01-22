package com.github.dcysteine.nesql.server.service;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.config.ExternalConfig;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Service providing values and utility methods to Thymeleaf templates. */
@Service
public class ThymeleafService {
    @Autowired
    private ExternalConfig externalConfig;

    @Autowired
    HttpServletRequest request;

    public ImmutableList<Table> getNavBarTables() {
        return Table.TABLES.get(Plugin.BASE);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String formatInteger(long integer) {
        return NumberUtil.formatInteger(integer);
    }

    public <T> List<List<T>> partitionGrid(List<T> results) {
        return Lists.partition(results, externalConfig.getPageSizeGridColumns());
    }
}
