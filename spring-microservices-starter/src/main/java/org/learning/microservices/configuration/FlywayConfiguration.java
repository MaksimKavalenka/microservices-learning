package org.learning.microservices.configuration;

import org.flywaydb.core.Flyway;
import org.learning.microservices.flyway.FlywayMigrationListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FlywayConfiguration {

    @Bean
    @ConditionalOnProperty(value = "spring.flyway.enabled", matchIfMissing = true)
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway, FlywayProperties flywayProperties,
                                                                 List<FlywayMigrationListener> flywayMigrationListeners) {
        return new FlywayMigrationInitializer(flyway, (f) -> {
            flywayMigrationListeners.forEach(FlywayMigrationListener::beforeMigration);

            if (flywayProperties.isEnabled()) {
                flyway.migrate();
            }

            flywayMigrationListeners.forEach(FlywayMigrationListener::afterMigration);
        });
    }

}
