package com.myththewolf.MCCBukkit.lib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
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

    /**
     * Waits for a response from the server, this will not be async!
     *
     * @return The result
     */
    public SocketResult complete() {
        try {
            return sendPacket().get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Waits for the socket to send and we get a response of some sort
     *
     * @param list The listener to invoke on
     */
    public void whenComplete(SocketResultListener list) {
        list.whenResult(complete());
    }

    /**
     * Tries to complete the socket request and waits for a response. This is async, and will not
     * block the thread. This also has a execution timeout of 10 seconds
     *
     * @param consumer The consumer to be run when we get a reponse or we reach a timeout
     */
    public void queue(SocketResultListener consumer) {
        Thread T = new Thread(() -> {
            SocketResult res;
            try {
                res = sendPacket().get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
                JSONObject err = new JSONObject();
                if (connectionSocket.isConnected()) {
                    err.put("status", "EMPTYRESPONSE");
                } else {
                    err.put("status", "TIMEDOUT");
                }
                res = new SocketResult(err);
                consumer.whenResult(res);
                return;
            }
            consumer.whenResult(res);
        });
        T.start();
    }

    /**
     * The future instainiation
     *
     * @return A instance of a future to complete the TCP socket connection
     */
    private Future<SocketResult> sendPacket() {
        return pool.submit(new Callable<SocketResult>() {
            @Override
            public SocketResult call() {
                try {
                    DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());
                    out.writeBytes(packet.toString() + "\n");
                    BufferedReader inFromClient =
                            new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


                    serverResponse = inFromClient.readLine();
                    System.out.print("RAW IN:::" + serverResponse);
                    try {

                        JSONObject result = new JSONObject(serverResponse);
                        return new SocketResult(result);
                    } catch (JSONException e) {
                        System.out.print(serverResponse);
                        // e.printStackTrace();
                        JSONObject ob = new JSONObject();
                        ob.put("response", serverResponse);
                        ob.put("status", "BADRESPONSE");
                        return new SocketResult(ob);
                    }
                } catch (IOException e) {
                    JSONObject err = new JSONObject();
                    err.put("status", "TIMEDOUT");
                    return new SocketResult(err);
                }

            }
        });

    }
}
