package com.myththewolf.MCCBukkit.lib;

import com.myththewolf.MCCBukkit.Main;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MCCPlayer {
    private String PLAYER_NAME;
    private String DISCORD_ID;
    private String UUID;
    private String SECRET_TOKEN;

    public MCCPlayer(String UUID) {
        try {
            Connection players = Main.PLAYER_DB.getConnection();
            PreparedStatement ps = players.prepareStatement("SELECT * FROM `MCC_Players` WHERE `UUID` = ?");
            ps.setString(1, UUID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PLAYER_NAME = rs.getString("username");
                DISCORD_ID = rs.getString("discord_id");
                this.UUID = rs.getString("UUID");
                SECRET_TOKEN = rs.getString("discord_secret");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public MCCPlayer(Player p){
        this(p.getUniqueId().toString());
    }

    public String getUsername(){
        return this.PLAYER_NAME;
    }

    public String getDiscordID(){
        return this.DISCORD_ID;
    }

    public String getUUID() {
        return UUID;
    }

    public String getDiscordSecret() {
        return SECRET_TOKEN;
    }

    public void setDiscordSecret(String SECRET_TOKEN) {
        this.SECRET_TOKEN = SECRET_TOKEN;
    }

    public void update(){
        try {
            Connection connection = Main.PLAYER_DB.getConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE `MCC_Players` set `discord_id` = ?, `username` = ?, `discord_secret` = ? WHERE `UUID` = ?");
            ps.setString(1,getDiscordID());
            ps.setString(2,getUsername());
            ps.setString(3,getDiscordSecret());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setDiscordID(String DISCORD_ID) {
        this.DISCORD_ID = DISCORD_ID;
    }
}
