package com.myththewolf.MCCBukkit.lib;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class SocketListener implements Runnable {
    private Socket connectionSocket;
    private static HashMap<String, SocketAdapter> listners = new HashMap<String, SocketAdapter>();

    public SocketListener(Socket con) {
        connectionSocket = con;
    }

    @Override
    public void run() {
        try {

            BufferedReader inFromServer1 =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            String inFromServer = inFromServer1.readLine();
            JSONObject parse = new JSONObject(inFromServer);
            if (parse.isNull("packetType")) {
                return;
            }
            listners.forEach((key, val) -> {
                parse.remove("packetType");
                val.onPacketRecieived(parse, this);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void registerPacketListener(String packetType, SocketAdapter adapter) {
        listners.put(packetType, adapter);
    }
}
