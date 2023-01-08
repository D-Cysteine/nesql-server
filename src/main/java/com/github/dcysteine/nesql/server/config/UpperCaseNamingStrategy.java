package com.github.dcysteine.nesql.server.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * For some reason, NESQL Exporter generates upper-case column and table names, but when we use the
 * exact same naming strategy here, Hibernate tries to use lower-case names. This is a quick fix.
 */
public class UpperCaseNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {
    @Override
    protected Identifier getIdentifier(String name, boolean quoted, JdbcEnvironment jdbcEnvironment) {
        return new Identifier(name.toUpperCase(), quoted);
    }
}
