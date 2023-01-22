package com.github.dcysteine.nesql.server.config;

import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Autowired
    ExternalConfig externalConfig;

    private static final String CONTENT_SECURITY_POLICY =
            Joiner.on("; ").join(
                    new String[]{
                            "default-src 'self'",
                            "img-src 'self' data:",
                    });

    @Bean
    @SuppressWarnings("deprecation")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers().contentSecurityPolicy(CONTENT_SECURITY_POLICY);

        if (!externalConfig.areExternalUsersAllowed()) {
            http.authorizeRequests().anyRequest()
                    .access("hasIpAddress('127.0.0.1') or hasIpAddress('::1')");
        }

        return http.build();
    }
}