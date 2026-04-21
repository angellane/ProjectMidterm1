/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server.controller;

import com.mycompany._24432024_server.model.ServerModel;
import java.util.concurrent.Callable;

/**
 *
 * @author fabia
 */
public class EarlyLectureWorker implements Callable<String> {

    private final ServerModel model;

    public EarlyLectureWorker(ServerModel model) {
        this.model = model;
    }

    @Override
    public String call() {
       return model.processEarlyLectures();
     }
}