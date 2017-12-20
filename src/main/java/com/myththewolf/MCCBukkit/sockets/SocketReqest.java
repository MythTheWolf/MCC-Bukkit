package com.myththewolf.MCCBukkit.sockets;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import org.json.JSONObject;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class SocketReqest {
    private Socket connection;
    private JSONObject packet;

    public SocketReqest(JSONObject pack, Socket gateway) {
        this.connection = gateway;
        this.packet = pack;
    }

    public void complete(SocketResultListener listen, int timeout) {
        try {
            submit().get(timeout, TimeUnit.SECONDS);
            PacketReceiver.registerWaitingRequest(UUID.randomUUID().toString(), listen);
        } catch (TimeoutException | InterruptedException | ExecutionException ex) {
            JSONObject resulter = new JSONObject();
            resulter.put("status", "INCOMPLETE");
            resulter.put("message", ex.getMessage());
            listen.onPacketResult(new SocketRequestResult(resulter));
        }
    }

    private Future<?> submit() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        return ex.submit(() -> {
            try {
                PrintWriter toClient =
                        new PrintWriter(connection.getOutputStream(), true);
                toClient.println(packet.toString());
            } catch (IOException e) {
                System.out.println("Could not send packet to server: " + packet + "::" + e.getMessage());
            }
        });
    }
}
