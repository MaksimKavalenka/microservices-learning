package org.learning.resource.configuration;

import org.flywaydb.core.Flyway;
import org.learning.resource.configuration.properties.DatabaseProperties;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class FlywayConfiguration {

    @Bean
    FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway, FlywayProperties flywayProperties,
                                                          DatabaseProperties databaseProperties,
                                                          DataSourceProperties dataSourceProperties) {
        return new FlywayMigrationInitializer(flyway, (f) -> {
            try {
                String sql = String.format("CREATE DATABASE %s;", databaseProperties.getName());

                DataSource dataSource = getDataSource(databaseProperties, dataSourceProperties);
                Connection connection = dataSource.getConnection();
                connection.prepareStatement(sql).execute();

                if (flywayProperties.isEnabled()) {
                    flyway.migrate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private DataSource getDataSource(DatabaseProperties databaseProperties, DataSourceProperties dataSourceProperties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(
                String.format("jdbc:postgresql://%s:%d/", databaseProperties.getHost(), databaseProperties.getPort()));
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        return dataSource;
    }

}
