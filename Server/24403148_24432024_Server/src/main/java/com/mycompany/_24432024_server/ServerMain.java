/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany._24432024_server;
import java.net.*;
import java.io.*;

/**
 *
 * @author fabia
 */
public class ServerMain {
    public static void main(String[] args) {
        try (ServerSocket servSock = new ServerSocket(1234)) {
            System.out.println("Server Started");
            
           while (true) {
               Socket link = servSock.accept();
               BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
               PrintWriter out = new PrintWriter(link.getOutputStream(), true);
               
               out.println("Awaiting data.");
               String input = in.readLine();
               System.out.println("Received: " + input);
                       
               link.close();
           }
        } catch (IOException e) {
            e.printStackTrace();
    }
    }
}
