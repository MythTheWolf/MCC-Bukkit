package com.myththewolf.MCCBukkit.events;

import com.myththewolf.MCCBukkit.Main;
import com.myththewolf.MCCBukkit.lib.SocketRequest;
import com.myththewolf.MCCBukkit.lib.StatusType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.json.JSONObject;

public class UserLeaveEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGH)
    public void onUserLeave(PlayerKickEvent event) {

        JSONObject packet = new JSONObject();
        packet.put("packetType", "user-leave");
        packet.put("username", event.getPlayer().getName());
        SocketRequest SR;
        SR = new SocketRequest(Main.connectionSocket, packet);
        SR.queue(result -> {
            if (result.getStatus() != StatusType.SUCCESS) {
                Main.getPlugin(Main.class).getLogger().severe("Packet deliver failed: " + packet.toString() + "(Result: " + result.getRawResult() + ")");
            } else {
                Main.getPlugin(Main.class).getLogger().info("Sent packet of type `user-leave` to the bot!");
            }
        });
    }
}
