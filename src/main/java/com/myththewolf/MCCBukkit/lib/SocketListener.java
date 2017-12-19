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
        while (true) {
            try {
                BufferedReader inFromServer1 =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String inFromServer = inFromServer1.readLine();
                System.out.println("GOT PACKET--->" + inFromServer);
                JSONObject parse = new JSONObject(inFromServer);

                if (parse.isNull("packetType")) {
                    if (!parse.isNull("ID") && SocketRequest.JOBS.containsKey(parse.getString("ID"))) {
                        JSONObject cut = new JSONObject(parse.toString());
                        cut.remove("ID");
                        SocketRequest.JOBS.get(parse.getString("ID")).whenResult(new SocketResult(cut));
                        SocketRequest.JOBS.remove(parse.getString("ID"));
                    }
                    continue;
                }
                listners.forEach((key, val) -> {
                    parse.remove("packetType");
                    val.onPacketRecieived(parse, this);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerPacketListener(String packetType, SocketAdapter adapter) {
        listners.put(packetType, adapter);
    }
}
