package com.myththewolf.MCCBukkit.lib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketRequest {
    public static HashMap<String, SocketResultListener> JOBS = new HashMap<>();
    /**
     * The packet to send, we do this in a field so we can access it in lambdas
     */
    private JSONObject packet;
    /**
     * The executor service for our Future<?> class
     */
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    /**
     * The socket connection to use
     */
    private Socket connectionSocket;
    private String serverResponse;

    /**
     * Constructs a new SocketRequest
     *
     * @param con    The socket in which to communicate on
     * @param packet The packet to deliver
     */
    public SocketRequest(Socket con, JSONObject packet) {
        if (packet.isNull("packetType"))
            throw new IllegalStateException("Packets must define a scope");

        this.connectionSocket = con;
        this.packet = packet;

    }

    public void whenCompleteOrError(SocketResultListener listener) {
        ExecutorService ex = Executors.newFixedThreadPool(20);
        Future<?> runner = ex.submit(() -> {
            Thread T = new Thread(() -> {
                System.out.print("Sending out packet:" + packet.toString());
                String uniqueID = UUID.randomUUID().toString();
                JOBS.put(uniqueID, listener);
                JSONObject out = new JSONObject();
                out.put("ID", uniqueID);
                out.put("data", packet.toString());
                try {
                    DataOutputStream out2 = new DataOutputStream(connectionSocket.getOutputStream());
                    out2.writeBytes(out.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            });
            T.start();
        });
        try {
            runner.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            JSONObject err = new JSONObject();
            err.put("status", "TIMEDOUT");
            listener.whenResult(new SocketResult(err));
        }
    }
}
