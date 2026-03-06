/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client;

import com.mycompany.client.controller.ClientController;
import com.mycompany.client.model.ClientModel;
import com.mycompany.client.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author b4zel
 */
public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        ClientView view = new ClientView(primaryStage);
        view.start();
        ClientModel model = new ClientModel();
        ClientController controller = new ClientController(view, model);
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}