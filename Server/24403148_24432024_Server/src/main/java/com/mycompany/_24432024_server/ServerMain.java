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
        ServerController cntrl = new ServerController();
        try (ServerSocket servSock = new ServerSocket(1234)) {
            System.out.println("Server Started");
            while (true) {
                Socket link = servSock.accept();
                System.out.println("Client connected: " + link.getInetAddress());
                
                ClientHandler handler = new ClientHandler(link, cntrl);
                Thread cThrd = new Thread(handler);
                cThrd.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
