package com.myththewolf.MCCBukkit.sockets;


import org.json.JSONObject;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketRequest {
    private Socket connection;
    private JSONObject packet;

    public SocketRequest(JSONObject pack, Socket gateway) {
        this.connection = gateway;
        this.packet = pack;
    }

    public void complete(SocketResultListener listen, int timeout) {
        try {
            String ID = UUID.randomUUID().toString();
            submit(ID).get(timeout, TimeUnit.SECONDS);
            PacketReceiver.registerWaitingRequest(ID, listen);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            JSONObject resulter = new JSONObject();
            resulter.put("status", "INCOMPLETE");
            resulter.put("message", ex.getMessage());
            listen.onPacketResult(new SocketRequestResult(resulter));
        }
    }

    private Future<?> submit(String ID) {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        return ex.submit(() -> {
            try {
                packet.put("ID", ID);
                PrintWriter toClient =
                        new PrintWriter(connection.getOutputStream(), true);
                toClient.println(packet.toString());
            } catch (IOException e) {
                System.out.println("Could not send packet to server: " + packet + "::" + e.getMessage());
            }
        });
    }
}
