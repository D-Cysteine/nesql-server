package com.github.dcysteine.nesql.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class holding config values that are meant to be externally configurable via the
 * {@code application.properties} file.
 */
@Component
public class ExternalConfig {
    @Value("${nesql.server.version}")
    private String version;

    @Value("${nesql.server.repository-name}")
    private String repositoryName;

    @Value("${nesql.server.info-panel-columns}")
    private int infoPanelColumns;

    @Value("${nesql.server.page-size.list}")
    private int pageSizeList;

    @Value("${nesql.server.page-size.grid-rows}")
    private int pageSizeGridRows;

    @Value("${nesql.server.page-size.grid-columns}")
    private int pageSizeGridColumns;

    @Value("${nesql.server.page-size.recipe}")
    private int pageSizeRecipe;

    @Value("${nesql.server.enable-shutdown}")
    private boolean shutdownEnabled;

    @Value("${nesql.server.allow-external-users}")
    private boolean externalUsersAllowed;

    public String getVersion() {
        return version;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public int getInfoPanelColumns() {
        return infoPanelColumns;
    }

    public int getPageSizeList() {
        return pageSizeList;
    }

    public int getPageSizeGridRows() {
        return pageSizeGridRows;
    }

    public int getPageSizeGridColumns() {
        return pageSizeGridColumns;
    }

    public int getPageSizeRecipe() {
        return pageSizeRecipe;
    }

    public boolean isShutdownEnabled() {
        return shutdownEnabled;
    }

    public boolean areExternalUsersAllowed() {
        return externalUsersAllowed;
    }
}
