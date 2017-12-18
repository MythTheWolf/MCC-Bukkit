package com.myththewolf.MCCBukkit.lib;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.json.JSONObject;

public class SocketRequestTest {

  public static void main(String[] args) {
    try {
      Socket con = new Socket("70.139.52.7", 6789);
      JSONObject packet = new JSONObject();
      packet.put("packetType", "test");
      SocketRequest SR = new SocketRequest(con, packet);
      SR.queue(result -> {
        if (result.getStatus() == StatusType.EMPTY_RESPONSE) {
          System.out.println("We got a empty respone...");
        } else if (result.getStatus() == StatusType.TIMED_OUT) {
          System.out.println("Could not connect to server.");
        }else {
          System.out.println(result.getRawResult());
        }
      });
      System.out.println("I AM THE NEXT LINE");


    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
