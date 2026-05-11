package com.project.artconnect.util;

import com.project.artconnect.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage JDBC connections.
 * TODO: Students must implementation the getConnection logic.
 */
public class ConnectionManager {

    /**
     * Returns a new JDBC connection to the ArtConnect database.
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        // DONE: return a JDBC connection using DriverManager
        return DriverManager.getConnection(DatabaseConfig.URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
    }
}
