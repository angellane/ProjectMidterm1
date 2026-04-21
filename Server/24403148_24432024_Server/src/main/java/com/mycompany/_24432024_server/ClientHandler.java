/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server;

import com.mycompany._24432024_server.controller.ServerController;
import java.io.*;
import java.net.*;

/**
 *
 * @author b4zel
 */
public class ClientHandler implements Runnable {
    private Socket link;
    private ServerController cntrl;
     
    public ClientHandler( Socket link, ServerController cntrl){
        this.link = link;
        this.cntrl = cntrl;
        
    }
    @Override
    public void run(){
                         
    try{
      
      BufferedReader in = new BufferedReader( new InputStreamReader(link.getInputStream())); 
      
      PrintWriter out = new PrintWriter(link.getOutputStream(),true); 
      
      String message;         
      while((message = in.readLine()) != null ){
      System.out.println("Message received from client: " + cntrl + "  "+ message);
        if (message.equalsIgnoreCase("quit")){
            out.println("Server closed by client: " + cntrl);
            break;
        } 
      out.println("Echo Message: " + message);     
     }
    }
    catch(IOException e) {
        e.printStackTrace();
    }
    finally 
    {
       try {
	    System.out.println("\n* Closing client connection... *");
            link.close();				    
	}
       catch(IOException e)
       {
        System.out.println("Unable to disconnect client : " + cntrl );
	    
       }
    }
  } 
}
