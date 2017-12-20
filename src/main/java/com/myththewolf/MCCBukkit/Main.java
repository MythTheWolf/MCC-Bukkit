package com.myththewolf.MCCBukkit;


import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.Socket;

public class Main extends JavaPlugin {
    public static Socket connectionSocket;

    @Override
    public void onEnable() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}