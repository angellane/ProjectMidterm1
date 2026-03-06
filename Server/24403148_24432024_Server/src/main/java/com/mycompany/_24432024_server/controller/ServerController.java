/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server.controller;
import com.mycompany._24432024_server.IncorrectActionException;
import com.mycompany._24432024_server.model.ServerModel;

/**
 *
 * @author fabia
 */
public class ServerController {
    private final ServerModel model;
    public ServerController() {
        this.model = new ServerModel();
    }
    public String processRequest(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "ERROR: Empty message received.";
        }
        String[] parts = message.trim().split("\\|");
        String action = parts[0].toUpperCase();
        try {
            switch (action) {
                case "ADD":
                    return handleAdd(parts);
                case "REMOVE":
                    return handleRemove(parts);
                case "DISPLAY":
                    return handleDisplay();
                case "STOP":
                    return handleStop();
                default:
                    throw new IncorrectActionException(parts[0]);
            }
        } catch (IncorrectActionException e) {
            System.err.println("IncorrectActionException caught: " + e.getMessage());
            return "EXCEPTION: " + e.getMessage();
        }
    }
    private String handleAdd(String[] parts) {
        
        String module = parts[1].trim();
        String date   = parts[2].trim();
        String time   = parts[3].trim();
        String room   = parts[4].trim();
        return model.addLecture(module, date, time, room);
    }
    private String handleRemove(String[] parts) {
        
        String module = parts[1].trim();
        String date   = parts[2].trim();
        String time   = parts[3].trim();
        String room   = parts[4].trim();
        return model.removeLecture(module, date, time, room);
    }
    private String handleDisplay() {
        return model.getScheduleSerialized();
    }
    private String handleStop() {
        System.out.println("STOP received. Closing Connection");
        return "Goodbye!";
    }
}
    

