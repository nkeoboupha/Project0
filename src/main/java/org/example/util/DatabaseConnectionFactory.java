package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionFactory {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static{
        try{
            String driver = System.getenv().getOrDefault("DB_DRIVER", "org.postgresql.Driver");
            Class.forName(driver);
        }catch(ClassNotFoundException e){
            throw new ExceptionInInitializerError("PostgreSQL Driver not found: " + e.getMessage());
        }
    }
    private DatabaseConnectionFactory() {}

    public static Connection getConnection() throws SQLException{
        if (URL == null || USER == null || PASSWORD == null){
            throw new SQLException("DB_URL, DB_USER, and DB_PASSWORD environment variables must be set");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
