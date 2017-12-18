package com.myththewolf.MCCBukkit;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
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
    } catch (UnknownHostException e) {
      getLogger().severe("Connection failed");
      Bukkit.getPluginManager().disablePlugin(this);
    } catch (IOException e) {
      getLogger().severe("Connection failed");
      Bukkit.getPluginManager().disablePlugin(this);

    }
    System.out.println("Sending test packet");

    JSONObject root = new JSONObject();
    root.put("packetType", "user-join");
    root.put("username", "@User[457b");
    try {
      DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
      out.writeBytes(root.toString() + "\n");
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}
