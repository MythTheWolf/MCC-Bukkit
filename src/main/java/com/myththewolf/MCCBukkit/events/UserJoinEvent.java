package com.myththewolf.MCCBukkit.events;

import com.myththewolf.MCCBukkit.lib.MCCPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UserJoinEvent implements Listener {
    @EventHandler
    public void onUserJoin(AsyncPlayerChatEvent event) {
        MCCPlayer MCP = new MCCPlayer(event.getPlayer());
        if (!MCP.isExistant()) {
            MCP.createPlayer();
        }
        MCP.setUsername(event.getPlayer().getName());
        MCP.update();
    }
}
