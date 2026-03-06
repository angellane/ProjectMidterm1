/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.model;
import java.io.*;
import java.net.*;

/**
 *
 * @author b4zel
 */
public class ClientModel {
    
        
    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean connected = false;

    
      public void connect() throws IOException {
        socket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        connected = true;
    }
      
         public void disconnect() {
        connected = false;
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }
    
      public String sendMessage(String message) throws IOException {
        if (!connected) {
            throw new IOException("Not connected to server.");
        }
        out.println(message);
        return in.readLine();
    }
      
      public static String buildAddMessage(String module, String date, String time, String room) {
        return "ADD|" + module + "|" + date + "|" + time + "|" + room;
    }

        public static String buildRemoveMessage(String module, String date, String time, String room) {
        return "REMOVE|" + module + "|" + date + "|" + time + "|" + room;
    }
      
      
}
