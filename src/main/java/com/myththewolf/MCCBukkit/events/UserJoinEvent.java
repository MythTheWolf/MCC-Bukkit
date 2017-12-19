package com.myththewolf.MCCBukkit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONObject;
import com.myththewolf.MCCBukkit.Main;
import com.myththewolf.MCCBukkit.lib.SocketRequest;
import com.myththewolf.MCCBukkit.lib.StatusType;

public class UserJoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        JSONObject packet = new JSONObject();
        packet.put("packetType", "user-join");
        packet.put("username", event.getPlayer().getName());
        SocketRequest SR = new SocketRequest(Main.connectionSocket, packet);
        SR.queue(result -> {
            if (result.getStatus() != StatusType.SUCCESS) {
                Main.getPlugin(Main.class).getLogger().severe("Packet delivery to server failed: "
                        + packet.toString() + "[Status: " + result.getRawResult() + "]");
            }else {
                Main.getPlugin(Main.class).getLogger().info("Sent packet of type `user-join` to the bot!");
            }
        });

    }
}
