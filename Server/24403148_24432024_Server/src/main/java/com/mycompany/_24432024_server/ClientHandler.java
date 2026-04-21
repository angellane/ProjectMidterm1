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
      System.out.println("Thread : "+ Thread.currentThread().threadId() + " Recieved "+ message);
     
      String resp = cntrl.processRequest(message);
      System.out.println("Thread : " + Thread.currentThread().threadId() +" Sending "+ resp);
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
       System.out.println("Thread : " + Thread.currentThread().threadId() + " Client connection closed. ");
    }
  } 
}
