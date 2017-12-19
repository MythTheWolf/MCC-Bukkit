package com.myththewolf.MCCBukkit.events;

import com.myththewolf.MCCBukkit.lib.SocketAdapter;
import com.myththewolf.MCCBukkit.lib.SocketListener;
import org.json.JSONObject;

public class UserChatSocketListener implements SocketAdapter {
    @Override
    public void onPacketRecieived(JSONObject message, SocketListener service) {
        System.out.print("got chat socket!");
    }
}
