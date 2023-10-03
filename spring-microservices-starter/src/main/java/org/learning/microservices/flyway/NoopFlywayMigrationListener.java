package org.learning.microservices.flyway;

public class NoopFlywayMigrationListener implements FlywayMigrationListener {

    @Override
    public void beforeMigration() {
    }

    @Override
    public void afterMigration() {
    }

}
