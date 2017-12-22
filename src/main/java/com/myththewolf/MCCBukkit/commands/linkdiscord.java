package com.myththewolf.MCCBukkit.commands;

import com.myththewolf.MCCBukkit.Main;
import com.myththewolf.MCCBukkit.lib.MCCPlayer;
import com.myththewolf.MCCBukkit.sockets.SocketRequest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class linkdiscord implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        JSONObject ob = new JSONObject();
        ob.put("packetType", "get-id-from-secret");
        ob.put("secret", args[0]);
        SocketRequest SR = new SocketRequest(ob, Main.getConnectionSocket());
        SR.complete(dd -> {
            JSONObject dat = dd.getData();
            if (!dd.getStatus().equals("OK")) {
                sender.sendMessage("A error has occurred while executing this command:");
                sender.sendMessage("The packet was delivered,but the server responded with: " + dd.getRaw().toString());
            } else {
                String ID = dat.getString("discord-id");
                MCCPlayer player = new MCCPlayer((Player) sender);
                player.setDiscordID(ID);
                player.update();
            }
        }, 1000);
        return false;
    }
}
