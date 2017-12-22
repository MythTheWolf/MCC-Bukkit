package com.myththewolf.MCCBukkit.lib;

import java.io.File;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteManager {
    private Connection conn = null;
    private String fileURL;

    public SQLiteManager(File dbloc) {
        fileURL = dbloc.getAbsolutePath();
    }
    public SQLiteManager(String path){
        this(new File(path));
    }
    /**
     * Connects to database.
     * If it does not exist, a new one is made.
     */
    public void connect() {
        try {
            // db parameters
            // create a connection to the database
            conn = DriverManager.getConnection(fileURL);
            System.out.println("Connected to SQLite database");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return conn;
    }

}
