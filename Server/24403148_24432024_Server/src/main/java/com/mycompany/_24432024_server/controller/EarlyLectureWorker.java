/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server.controller;

import com.mycompany._24432024_server.model.ServerModel;
import javafx.concurrent.Task;

/**
 *
 * @author fabia
 */
public class EarlyLectureWorker extends Task<String> {

    private final ServerModel model;

    public EarlyLectureWorker(ServerModel model) {
        this.model = model;
    }

    @Override
    protected String call() {
        updateMessage("Processing early lectures...");
        String result = model.processEarlyLectures();
        updateMessage("Early lectures processing complete.");
        return result;
    }
}