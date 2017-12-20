package com.myththewolf.MCCBukkit.sockets;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class PacketReceiver implements Runnable {
    private Socket connection;
    private static HashMap<String, SocketResultListener> jobs = new HashMap<>();
    private static HashMap<String, SocketPacketHandler> handlers = new HashMap<>();

    public PacketReceiver(Socket gateway) {
        this.connection = gateway;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BufferedReader fromServer =
                        new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                String data = fromServer.readLine();
                JSONObject ob = new JSONObject(data);
                if (ob.getString("packetType").equals("PACKET_RESULT") && jobs.containsKey(ob.getString("ID"))) {
                    JSONObject tmp = new JSONObject(ob.toString());
                    tmp.remove("packetType");
                    jobs.get(ob.getString("ID")).onPacketResult(new SocketRequestResult(tmp));
                    continue;
                }
                if (handlers.containsKey(ob.getString("packetType"))) {
                    JSONObject tmp = new JSONObject(ob.toString());
                    tmp.remove("packetType");
                    handlers.get(ob.getString("packetType")).onPacketReceived(tmp);
                    continue;
                } else {
                    System.out.print("Got unknown packet: " + data);
                }
            } catch (IOException e) {
                System.out.println("Fatal error in PacketReceiver:" + e.getMessage());
            }
        }
    }

    public static void registerWaitingRequest(String ID, SocketResultListener list) {
        jobs.put(ID, list);
    }

    public static void registerPacketHandler(String packetType, SocketPacketHandler handler) {
        handlers.put(packetType, handler);
    }
}
