package com.myththewolf.MCCBukkit;


import com.myththewolf.MCCBukkit.sockets.packetHandlers.DiscordChatIn;
import com.myththewolf.MCCBukkit.sockets.PacketReceiver;
import com.myththewolf.MCCBukkit.sockets.SocketReqest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Main extends JavaPlugin {
    private static Socket connectionSocket;

    @Override
    public void onEnable() {
        try {
            if (!getDataFolder().exists()) {
                boolean res = getDataFolder().mkdirs();
                if (!res) {
                    getLogger().severe("Could not generate plugin folder!");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            Bukkit.getPluginManager().disablePlugin(this);
            e.printStackTrace();
        }
        try {
            connectionSocket = new Socket("70.139.52.7", 6789);
        } catch (IOException e) {
            Bukkit.getPluginManager().disablePlugin(this);
            e.printStackTrace();
            return;
        }
        PacketReceiver.registerPacketHandler("user-chat", new DiscordChatIn());
        Bukkit.getScheduler().runTaskAsynchronously(this, new PacketReceiver(connectionSocket));
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                JSONObject request = new JSONObject();
                request.put("packetType", "user-chat");
                request.put("message", event.getMessage());
                System.out.print(request.getString("message"));
                SocketReqest SR = new SocketReqest(request, connectionSocket);
                SR.complete(test -> System.out.print("...Completed"), 10);
            }
        }, this);
    }
}