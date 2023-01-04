package com.github.dcysteine.nesql.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class holding config values that are meant to be externally configurable via the
 * {@code application.properties} file.
 */
@Component
public class ExternalConfig {
    @Value("${nesql.server.repository-name}")
    private String repositoryName;

    @Value("${nesql.server.page-size}")
    private int pageSize;

    @Value("${nesql.server.enable-shutdown}")
    private boolean shutdownEnabled;

    @Value("${nesql.server.allow-external-users}")
    private boolean externalUsersAllowed;

    public String getRepositoryName() {
        return repositoryName;
    }

    public int getPageSize() {
        return pageSize;
    }

    public boolean isShutdownEnabled() {
        return shutdownEnabled;
    }

    public boolean areExternalUsersAllowed() {
        return externalUsersAllowed;
    }
}
