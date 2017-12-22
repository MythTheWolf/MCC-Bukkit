package com.myththewolf.MCCBukkit.lib;

import com.myththewolf.MCCBukkit.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MCCPlayer {
    private String PLAYER_NAME;
    private String DISCORD_ID;
    private String UUID;
    private boolean exists;

    public MCCPlayer(String UUID) {
        try {
            Connection players = Main.PLAYER_DB.getConnection();
            PreparedStatement ps = players.prepareStatement("SELECT * FROM `MCC_Players` WHERE `UUID` = ?");
            ps.setString(1, UUID);
            ResultSet rs = ps.executeQuery();
            boolean test = false;
            this.UUID = UUID;
            while (rs.next()) {
                PLAYER_NAME = rs.getString("username");
                DISCORD_ID = rs.getString("discord_id");
                test = true;
            }
            exists = test;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MCCPlayer(Player p) {
        this(p.getUniqueId().toString());
    }

    public boolean isExistant() {
        return exists;
    }

    public String getUsername() {
        return this.PLAYER_NAME;
    }

    public String getDiscordID() {
        return this.DISCORD_ID;
    }

    public String getUUID() {
        return UUID;
    }


    public void update() {
        try {
            Connection connection = Main.PLAYER_DB.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE `MCC_Players` set `discord_id` = ?, `username` = ?, WHERE `UUID` = ?");
            ps.setString(1, getDiscordID());
            ps.setString(2, getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer() {
        try {
            PreparedStatement ps = Main.PLAYER_DB.getConnection().prepareStatement("INSERT INTO `MCC_Players` (`UUID`,`username`) VALUES (?,?)");
            ps.setString(1, this.UUID);
            ps.setString(2, Bukkit.getPlayer(java.util.UUID.fromString(this.UUID)).getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDiscordID(String DISCORD_ID) {
        this.DISCORD_ID = DISCORD_ID;
    }

    public void setUsername(String PLAYER_NAME) {
        this.PLAYER_NAME = PLAYER_NAME;
    }
}
