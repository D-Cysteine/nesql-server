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

    private static final String CONTENT_SECURITY_POLICY = Joiner.on("; ").join(
            "default-src 'self'",
            // This is the hash of our image onerror handler:
            // this.src='/image/missing.png';this.onerror='';
            "script-src 'self' 'sha256-KrWF/SaFRimyTg5FZWV3gqZlxM6hGYkkM1k3ePvN71w=' 'unsafe-hashes'");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO restrict requests to same-IP only? At least by default
        http.headers().contentSecurityPolicy(CONTENT_SECURITY_POLICY);

        if (!externalConfig.areExternalUsersAllowed()) {
            http.authorizeRequests().anyRequest()
                    .access("hasIpAddress('127.0.0.1') or hasIpAddress('::1')");
        }

        return http.build();
    }
}