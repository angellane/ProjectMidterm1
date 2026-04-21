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
    private static int clientCounter = 0;
    private final int clientNum;
    private final Socket link;
    private final ServerController cntrl;
     
    public ClientHandler( Socket link, ServerController cntrl){
        this.link = link;
        this.cntrl = cntrl;
        this.clientNum = ++clientCounter;
        
    }
    @Override
    public void run(){
                         
    try{
      
      BufferedReader in = new BufferedReader( new InputStreamReader(link.getInputStream())); 
      
      PrintWriter out = new PrintWriter(link.getOutputStream(),true); 
      
      String message;         
      while((message = in.readLine()) != null ){
      System.out.println("Client : "+ clientNum + " Recieved "+ message);
     
      String resp = cntrl.processRequest(message);
      System.out.println("Client : " + clientNum +" Sending "+ resp);
     out.println(resp); 
      if (resp.startsWith("TERMINATE"))  break;
         
     }
    }catch(IOException e){
        System.out.println("Client ERROR: " + e.getMessage());
        
    }finally{
       try {
            link.close();				    
	} catch(IOException e)  {
           e.printStackTrace();
            
       }
       System.out.println("Client : " + clientNum + " Client connection closed. ");
    }
  } 
}
