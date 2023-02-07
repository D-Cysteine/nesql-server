package com.github.dcysteine.nesql.server.common.service;

import com.github.dcysteine.nesql.server.common.Table;
import com.github.dcysteine.nesql.server.common.util.NumberUtil;
import com.github.dcysteine.nesql.server.config.ExternalConfig;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.MetadataRepository;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

/** Service providing values and utility methods to Thymeleaf templates. */
@Service
public class ThymeleafService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static ImmutableList<ImmutableList<Table>> navbarDropdownTables = null;

    @Autowired
    private ExternalConfig externalConfig;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    HttpServletRequest request;

    public ImmutableList<Table> getNavbarMainTables() {
        return Table.TABLES.get(Plugin.BASE);
    }

    public ImmutableList<ImmutableList<Table>> getNavbarDropdownTables() {
        if (navbarDropdownTables != null) {
            return navbarDropdownTables;
        }

        Metadata metadata = metadataRepository.findById(Metadata.ID).get();
        ImmutableList.Builder<ImmutableList<Table>> builder = ImmutableList.builder();
        for (Plugin plugin : metadata.getActivePlugins()) {
            if (plugin == Plugin.BASE || Table.TABLES.get(plugin).isEmpty()) {
                continue;
            }

            builder.add(Table.TABLES.get(plugin));
        }

        navbarDropdownTables = builder.build();
        return navbarDropdownTables;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public boolean getDarkMode() {
        return externalConfig.getDarkMode();
    }

    public String formatInteger(long integer) {
        return NumberUtil.formatInteger(integer);
    }

    public String formatTimestamp(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    public <T> List<List<T>> partitionInfoPanels(List<T> infoPanels) {
        return Lists.partition(infoPanels, externalConfig.getInfoPanelColumns());
    }

    public <T> List<List<T>> partitionGrid(List<T> results) {
        return Lists.partition(results, externalConfig.getPageSizeGridColumns());
    }
}
