package com.myththewolf.MCCBukkit.lib;

import org.json.JSONObject;

public interface SocketAdapter {
    public void onPacketRecieived(JSONObject message, SocketListener service);
}
