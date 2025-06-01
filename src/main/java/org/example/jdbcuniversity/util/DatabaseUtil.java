package org.example.jdbcuniversity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);

    public static final String URL = "jdbc:postgresql://localhost:5432/liquibase_university";
    public static final String USER = "postgres";
    public static final String PASSWORD = "2212";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            log.debug("Установлено подключение к БД: {}", URL);
            return connection;
        } catch (SQLException e) {
            log.error("Не удалось подключиться к базе данных по URL: {}", URL, e);
            throw new RuntimeException("Ошибка подключения к БД", e);
        }
    }
}