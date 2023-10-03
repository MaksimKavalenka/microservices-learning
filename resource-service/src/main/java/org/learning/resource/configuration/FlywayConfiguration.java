package org.learning.resource.configuration;

import lombok.extern.slf4j.Slf4j;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Configuration
@Slf4j
public class FlywayConfiguration {

    @Bean
    FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway, FlywayProperties flywayProperties,
                                                          DatabaseProperties databaseProperties,
                                                          DataSourceProperties dataSourceProperties) {
        return new FlywayMigrationInitializer(flyway, (f) -> {
            String selectDatabaseSql = String.format("SELECT FROM pg_database WHERE datname = '%s'", databaseProperties.getName());
            String createDatabaseSql = String.format("CREATE DATABASE %s", databaseProperties.getName());

            DataSource dataSource = getDataSource(databaseProperties, dataSourceProperties);

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement selectDatabaseStatement = connection.prepareStatement(selectDatabaseSql);
                 PreparedStatement createDatabaseStatement = connection.prepareStatement(createDatabaseSql)) {

                if (!selectDatabaseStatement.executeQuery().next()) {
                    createDatabaseStatement.executeUpdate();
                    log.info("Database has been created: {}", databaseProperties.getName());
                } else {
                    log.info("Database already exists: {}", databaseProperties.getName());
                }

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
