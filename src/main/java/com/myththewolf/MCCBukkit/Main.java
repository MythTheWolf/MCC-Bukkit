package com.myththewolf.MCCBukkit;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.myththewolf.MCCBukkit.events.UserChatEvent;
import com.myththewolf.MCCBukkit.events.UserChatSocketListener;
import com.myththewolf.MCCBukkit.events.UserJoinEvent;
import com.myththewolf.MCCBukkit.events.UserLeaveEvent;
import com.myththewolf.MCCBukkit.lib.SocketAdapter;
import com.myththewolf.MCCBukkit.lib.SocketListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

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


        try {
            getLogger().info("Attempting connection to " + getConfig().getString("server") + ":"
                    + getConfig().getInt("port"));
            connectionSocket = new Socket(getConfig().getString("server"), getConfig().getInt("port"));
        } catch (IOException e) {
            getLogger().severe("Connection failed");
            Bukkit.getPluginManager().disablePlugin(this);

        }

        Bukkit.getPluginManager().registerEvents(new UserChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new UserJoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new UserLeaveEvent(), this);
   //     Bukkit.getScheduler().runTaskAsynchronously(this, new SocketListener(connectionSocket));
        SocketListener.registerPacketListener("message", new UserChatSocketListener());
    }
}