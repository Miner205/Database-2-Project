package com.project.artconnect.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration constants.
 * TODO: Students should update these with their own MySQL credentials.
 */
public class DatabaseConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/artconnect";
    public static final String USER = "root";
    public static final String PASSWORD = "t"; // my mysql db password

    /** Private constructor: utility class, no instantiation. */
    private DatabaseConfig() {}

    /**
     * Returns a new JDBC connection to the ArtConnect database.
     *
     * @return an open {@link Connection}
     * @throws SQLException if the connection fails
     *                      DONE: completed this method.
     */
    public static Connection getConnection() throws SQLException {
        // DONE: return a JDBC connection using DriverManager
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
