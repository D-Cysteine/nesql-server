package com.github.dcysteine.nesql.server.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * For some reason, even though NESQL Exporter also uses CamelCaseToUnderscoresNamingStrategy,
 * we get an error where NESQL Server is putting quotes around some column names.
 * This class allows us to manually fix this.
 */
public class CustomNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
    @Override
    protected Identifier getIdentifier(
            String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        return super.getIdentifier(name, false, jdbcEnvironment);
    }
}
