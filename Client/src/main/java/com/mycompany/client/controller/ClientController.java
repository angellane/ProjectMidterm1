/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.controller;

import com.mycompany.client.model.ClientModel;
import com.mycompany.client.view.ClientView;
import java.io.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author b4zel
 */
public class ClientController {

    private final ClientView  view;
    private final ClientModel model;

    private void handleResponse(String action, String response) {
       if (response == null) { view.appendResponse("[Error] No response from server."); return; }
        if (!response.startsWith("SCHEDULE|")) view.appendResponse("[Received] " + response);
        if (action.equals("DISPLAY") && response.startsWith("SCHEDULE|")) {
            view.updateSchedule(response);
            String[] parts = response.split("\\|");
        int count = Integer.parseInt(parts[1]);
            view.appendResponse("[Display] " + count + " lecture(s) scheduled:");
        for (int i = 0; i < count; i++) {
        int base = 2 + i * 5;
        if (base + 3 < parts.length)
            view.appendResponse("  " + parts[base] + " | " + parts[base+1] + " | " + parts[base+2] + " | " + parts[base+3]);
    }
}
        if (response.startsWith("TERMINATE")) {
            view.setStatus("Connection terminated by server.");
            view.sendBtn.setDisable(true);
            view.stopBtn.setDisable(true);
            view.connectBtn.setDisable(false);
            model.disconnect();
        }
        if (response.startsWith("EXCEPTION")) {
            view.appendResponse("[!] IncorrectActionException thrown on server.");
        }
        
    }

    public ClientController(ClientView view, ClientModel model) {
        this.view  = view;
        this.model = model;

        view.actionBox.setOnAction(e -> view.showLectureInputPanel(
            view.getSelectedAction().equals("ADD") || view.getSelectedAction().equals("REMOVE")
        ));

        view.connectBtn.setOnAction(e -> {
            try {
                model.connect();
                view.setStatus("Connected to server on localhost:1234");
                view.appendResponse("[System] Success: Connected to server! ");
                view.sendBtn.setDisable(false);
                view.stopBtn.setDisable(false);
                view.connectBtn.setDisable(true);
            } catch (IOException ex) {
                view.appendResponse("[ERROR] Could not connect: " + ex.getMessage());
                view.setStatus("Connection failed: " + ex.getMessage());
            }
        });

        view.sendBtn.setOnAction(e -> {
            if (!model.isConnected()) { view.appendResponse("[WARNING] Not connected."); return; }
            String action = view.getSelectedAction();
            if (!action.equals("DISPLAY") && !action.equals("OTHER")) {
                if (view.getRoomNumber().isEmpty() || view.getSelectedModule().isEmpty() || view.getSelectedTime() == null ){ 
                    view.appendResponse("[WARNING] Please enter fields for Room, Module, and/or Time."); 
                    alertWarn("Please enter Room, Module and/or Time before sending.");
                    return; 
               
                }
            }
            String message;
            switch (action) {
                case "ADD": message = "ADD|"+ view.getSelectedModule() + "|" + view.getSelectedDate() + "|" + view.getSelectedTime() + "|" + view.getRoomNumber(); break;
                case "REMOVE": message = "REMOVE|" + view.getSelectedModule() + "|" + view.getSelectedDate() + "|" + view.getSelectedTime() + "|" + view.getRoomNumber(); break;
                case "DISPLAY": message = "DISPLAY" ; break;
                case "EARLY": message = "EARLY"; break;
                default: message = "OTHER"; break;
            }
            view.appendResponse("[Sent] " + message);
            try {
                String response = model.sendMessage(message);
                handleResponse(action, response);
            } catch (IOException ex) {
                view.appendResponse("[Error] " + ex.getMessage());
                view.setStatus("Communication error.");
            }
        });

        view.stopBtn.setOnAction(e -> {
            if (!model.isConnected()) { view.setStatus("Not connected.");
            return;
            }
            
            try {
                String response = model.sendMessage("STOP");
                view.appendResponse("[Sent] STOP");
                view.appendResponse("[Received] " + response);
                view.setStatus("Disconnected from server.");
                view.sendBtn.setDisable(true);
                view.stopBtn.setDisable(true);
                view.connectBtn.setDisable(false);
                model.disconnect();
            } catch (IOException ex) {
                view.appendResponse("[Error] " + ex.getMessage());
                model.disconnect();
            }
        });
        view.clearBtn.setOnAction(e -> view.clearInputs());
    }
         

        private void alertWarn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText("Validation");
        a.showAndWait();
    }

        private void alertInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText("Info");
        a.showAndWait();
    }
     
}
