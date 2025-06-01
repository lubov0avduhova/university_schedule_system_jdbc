package org.example.jdbcuniversity.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseInitializer {
    public static void liquibaseRun() {

        try (Connection conn = DriverManager.getConnection(DatabaseUtil.URL,DatabaseUtil.USER, DatabaseUtil.PASSWORD)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            System.out.println("Liquibase миграции применены!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
