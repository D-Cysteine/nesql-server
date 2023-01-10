package com.github.dcysteine.nesql.server.config;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.spi.TypeConfiguration;

import static org.hibernate.query.sqm.produce.function.FunctionParameterType.STRING;

/** Custom SQL dialect which adds support for regex matching. */
public class SqlDialect extends H2Dialect {
    @Override
    public void initializeFunctionRegistry(QueryEngine queryEngine) {
        super.initializeFunctionRegistry(queryEngine);

        SqmFunctionRegistry functionRegistry = queryEngine.getSqmFunctionRegistry();
        TypeConfiguration typeConfiguration = queryEngine.getTypeConfiguration();
        BasicTypeRegistry basicTypeRegistry = typeConfiguration.getBasicTypeRegistry();

        // http://www.h2database.com/html/functions.html#regexp_like
        functionRegistry.namedDescriptorBuilder("regexp_like")
                .setInvariantType(basicTypeRegistry.resolve(StandardBasicTypes.BOOLEAN))
                .setArgumentCountBetween(2, 3)
                .setParameterTypes(STRING, STRING, STRING)
                .setArgumentListSignature(
                        "(STRING inputString, STRING regexString[, String flagsString])")
                .register();
    }
}
