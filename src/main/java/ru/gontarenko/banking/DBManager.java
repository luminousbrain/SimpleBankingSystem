package ru.gontarenko.banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();
    private static String URL = "jdbc:sqlite:";

    public static void setUrl(String fileName) {
        dataSource.setUrl(URL + fileName);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

