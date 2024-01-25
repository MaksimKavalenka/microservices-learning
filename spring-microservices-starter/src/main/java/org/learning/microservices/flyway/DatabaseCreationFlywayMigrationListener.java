package org.learning.microservices.flyway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learning.microservices.configuration.properties.DatabaseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
@ConditionalOnProperty("flyway.database.create-if-not-exists")
@RequiredArgsConstructor
@Slf4j
public class DatabaseCreationFlywayMigrationListener extends NoopFlywayMigrationListener {

    private final DatabaseProperties databaseProperties;

    private final DataSource dataSource;

    @Override
    public void beforeMigration() {
        String selectDatabaseSql = String.format("SELECT FROM pg_database WHERE datname = '%s'", databaseProperties.getName());
        String createDatabaseSql = String.format("CREATE DATABASE %s", databaseProperties.getName());

        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectDatabaseStatement = connection.prepareStatement(selectDatabaseSql);
             PreparedStatement createDatabaseStatement = connection.prepareStatement(createDatabaseSql)) {

            if (!selectDatabaseStatement.executeQuery().next()) {
                createDatabaseStatement.executeUpdate();
                log.info("Database has been created: {}", databaseProperties.getName());
            } else {
                log.info("Database already exists: {}", databaseProperties.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
