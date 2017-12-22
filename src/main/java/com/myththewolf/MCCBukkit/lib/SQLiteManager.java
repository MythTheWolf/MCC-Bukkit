package com.myththewolf.MCCBukkit.lib;

import java.io.File;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLiteManager {
    private Connection conn = null;
    private String fileURL;

    public SQLiteManager(File dbloc) {
        fileURL = dbloc.getAbsolutePath();
    }

    public SQLiteManager(String path) {
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
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileURL);
            System.out.println("Connected to SQLite database");
            checkTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return conn;
    }


    public void checkTables() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `MCC_Players` ( `ID` INT NOT NULL AUTO_INCREMENT , `username` VARCHAR(255) NULL , `discord_id` VARCHAR(255) NULL DEFAULT NULL , `UUID` VARCHAR(255) NOT NULL , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
