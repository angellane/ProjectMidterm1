/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.controller;

import com.mycompany.client.model.ClientModel;
import com.mycompany.client.view.ClientView;
import java.io.*;

/**
 *
 * @author b4zel
 */
public class ClientController {

    private final ClientView  view;
    private final ClientModel model;

    private void handleResponse(String action, String response) {
        if (response == null) { view.appendResponse("[Error] No response from server."); return; }
        view.appendResponse("[Received] " + response);
        if (action.equals("DISPLAY") && response.startsWith("SCHEDULE|")) {
            view.updateSchedule(response);
            view.appendResponse("[System] Timetable updated.");
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
                view.setStatus("Connected to server on localhost:5000");
                view.appendResponse("[System] Connected to server successfully.");
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
                if (view.getRoomNumber().isEmpty())     { view.appendResponse("[WARNING] Please enter a room number."); return; }
                if (view.getSelectedModule().isEmpty()) { view.appendResponse("[WARNING] Please enter a module name."); return; }
            }
            String message;
            switch (action) {
                case "ADD":     message = "ADD|"    + view.getSelectedModule() + "|" + view.getSelectedDate() + "|" + view.getSelectedTime() + "|" + view.getRoomNumber(); break;
                case "REMOVE":  message = "REMOVE|" + view.getSelectedModule() + "|" + view.getSelectedDate() + "|" + view.getSelectedTime() + "|" + view.getRoomNumber(); break;
                case "DISPLAY": message = "DISPLAY"; break;
                default:        message = "UNKNOWN_ACTION"; break;
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
            if (!model.isConnected()) { view.setStatus("Not connected."); return; }
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

        view.clearBtn.setOnAction(e -> view.clearSchedule());
    }
}
