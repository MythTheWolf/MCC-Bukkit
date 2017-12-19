package com.myththewolf.MCCBukkit.events;

import com.myththewolf.MCCBukkit.Main;
import com.myththewolf.MCCBukkit.lib.SocketRequest;
import com.myththewolf.MCCBukkit.lib.StatusType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONObject;

public class UserChatEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        JSONObject packet = new JSONObject();
        packet.put("packetType", "user-chat");
        packet.put("username", event.getPlayer().getName());
        packet.put("message", event.getMessage());
        SocketRequest SR = new SocketRequest(Main.connectionSocket, packet);
        SR.whenCompleteOrError(result -> {
            if (result.getStatus() != StatusType.SUCCESS) {
                Main.getPlugin(Main.class).getLogger().severe("Packet delivery to server failed: "
                        + packet.toString() + "[Status: " + result.getRawResult() + "]");
            } else {
                Main.getPlugin(Main.class).getLogger().info("Sent packet of type `user-join` to the bot!");
            }
        });
    }
}
