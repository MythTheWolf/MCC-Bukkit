package com.myththewolf.MCCBukkit.sockets;

import org.json.JSONObject;

public interface SocketPacketHandler {
    public void onPacketReceived(JSONObject data);
}
