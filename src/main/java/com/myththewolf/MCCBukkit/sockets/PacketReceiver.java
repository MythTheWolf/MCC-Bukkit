package com.myththewolf.MCCBukkit.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PacketReceiver implements Runnable {
    private Socket connection;

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
                System.out.println("Received packet: " + data);
            } catch (IOException e) {
                System.out.println("Fatal error in PacketReceiver:" + e.getMessage());
            }
        }
    }
}
