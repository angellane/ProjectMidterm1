/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server.model;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author fabia
 */
public class EarlyLectureTask extends RecursiveAction {

    private final List<String> dates;
    private final ServerModel model;

    public EarlyLectureTask(List<String> dates, ServerModel model) {
        this.dates = dates;
        this.model = model;
    }

    @Override
    protected void compute() {
        if (dates.size() <= 1) {
            if (!dates.isEmpty()) {
                model.shiftDayEarly(dates.get(0));
            }
            return;
        }
        int mid = dates.size() / 2;
        EarlyLectureTask left  = new EarlyLectureTask(dates.subList(0, mid), model);
        EarlyLectureTask right = new EarlyLectureTask(dates.subList(mid, dates.size()), model);
        left.fork();
        right.compute();
        left.join();
    }
}