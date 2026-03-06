/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany._24432024_server;

import com.mycompany._24432024_server.controller.ServerController;
import java.net.*;
import java.io.*;

/**
 *
 * @author fabia
 */
public class ServerMain {

    public static void main(String[] args) {
        ServerController controller = new ServerController();
        try (ServerSocket servSock = new ServerSocket(1234)) {
            System.out.println("Server Started");
            while (true) {
                Socket link = servSock.accept();
                System.out.println("Client connected: " + link.getInetAddress());
                BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
                PrintWriter out = new PrintWriter(link.getOutputStream(), true);
                String input;
                while ((input = in.readLine()) != null) {
                    System.out.println("Received: " + input);
                    String response = controller.processRequest(input);
                    System.out.println("Sending: " + response);
                    out.println(response);
                    if (response.startsWith("TERMINATE")) break;
                }
                link.close();
                System.out.println("Client disconnected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
