package com.bafix.jobtrackerbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StartupChecks implements ApplicationRunner {

    @Value("${app.jwt.secret:}")
    private String jwtSecret;

    private final Environment env;

    public StartupChecks(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] profiles = env.getActiveProfiles();
        boolean isProd = Arrays.stream(profiles)
                .anyMatch(p -> p.equalsIgnoreCase("prod") || p.equalsIgnoreCase("production"));

        if (isProd && (jwtSecret == null || jwtSecret.isBlank())) {
            throw new IllegalStateException("APP_JWT_SECRET must be set in production (env APP_JWT_SECRET or app.jwt.secret).");
        }
    }
}
