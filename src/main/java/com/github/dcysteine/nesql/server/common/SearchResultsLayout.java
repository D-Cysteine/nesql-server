package com.github.dcysteine.nesql.server.common;

import com.github.dcysteine.nesql.server.config.ExternalConfig;

public enum SearchResultsLayout {
    LIST {
        @Override
        public int getPageSize(ExternalConfig externalConfig) {
            return externalConfig.getPageSizeList();
        }
    },

    GRID {
        @Override
        public int getPageSize(ExternalConfig externalConfig) {
            return externalConfig.getPageSizeGridRows()
                    * externalConfig.getPageSizeGridColumns();
        }
    },

    RECIPE {
        @Override
        public int getPageSize(ExternalConfig externalConfig) {
            return externalConfig.getPageSizeRecipe();
        }
    },
    ;

    public abstract int getPageSize(ExternalConfig externalConfig);
}