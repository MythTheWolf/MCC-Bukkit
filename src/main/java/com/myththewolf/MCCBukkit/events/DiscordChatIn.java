package com.myththewolf.MCCBukkit.events;

import com.myththewolf.MCCBukkit.sockets.SocketPacketHandler;
import org.bukkit.Bukkit;
import org.json.JSONObject;

public class DiscordChatIn implements SocketPacketHandler {
    @Override
    public void onPacketReceived(JSONObject data) {
        String DISCORD_ID = data.getString("discord-id");
        String CONTENT = data.getString("content");
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.sendMessage(data.toString());
        });
    }
}
