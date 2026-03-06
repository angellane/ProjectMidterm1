/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server.model;
/**
 *
 * @author fabia
 */
public class Lecture {
    private String module;
    private String date;
    private String time;
    private String roomNumber;
    private String course;
    public Lecture(String module, String date, String time, String roomNumber, String course) {
        this.module = module;
        this.date = date;
        this.time = time;
        this.roomNumber = roomNumber;
        this.course = course;
    }
    public String getModule()     { return module; }
    public String getDate()       { return date; }
    public String getTime()       { return time; }
    public String getRoomNumber() { return roomNumber; }
    public String getCourse()     { return course; }
    public String getRoomTimeKey() {
        return date.toUpperCase() + "|" + time + "|" + roomNumber.toUpperCase();
    }
    public String getCourseTimeKey() {
        return course.toUpperCase() + "|" + date.toUpperCase() + "|" + time;
    }
    @Override
    public String toString() {
        return course + "|" + module + "|" + date + "|" + time + "|" + roomNumber;
    }
    public String serialize() {
        return module + "|" + date + "|" + time + "|" + roomNumber + "|" + course;
        
    }
    public static Lecture deserialize(String data) {
        String[] parts = data.split("~");
        if (parts.length != 5) return null;
        return new Lecture(parts[0], parts[1], parts[2], parts[3], parts[4]);
    }
}
