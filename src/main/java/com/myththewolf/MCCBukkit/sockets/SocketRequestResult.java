package com.myththewolf.MCCBukkit.sockets;

import org.json.JSONObject;

public class SocketRequestResult {
    private String status;
    private JSONObject data;
    private String message;
    private JSONObject raw;
    public SocketRequestResult(JSONObject pack) {
        raw = new JSONObject(pack.toString());
        status = pack.getString("status");
        message = !pack.isNull("message") ? pack.getString("message") : "NO MESSAGE PROVIDED!";
        data.remove("status");
        if(!pack.isNull("message")){
            pack.remove("message");
        }
        data = pack;
    }

    public JSONObject getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
    public JSONObject getRaw(){
        return this.raw;
    }
}
