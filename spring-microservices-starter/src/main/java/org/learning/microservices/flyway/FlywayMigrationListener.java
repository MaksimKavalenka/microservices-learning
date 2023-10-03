package org.learning.microservices.flyway;

public interface FlywayMigrationListener {

    void beforeMigration();

    void afterMigration();

}
