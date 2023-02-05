package com.github.dcysteine.nesql.server;

import com.github.dcysteine.nesql.sql.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = Metadata.class)
@EnableJpaRepositories(basePackageClasses = Metadata.class)
public class Main {
    public static final Logger Logger = LoggerFactory.getLogger("NESQL Server");

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}