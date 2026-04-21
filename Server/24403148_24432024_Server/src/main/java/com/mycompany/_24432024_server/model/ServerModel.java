/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany._24432024_server.model;

/**
 *
 * @author fabia
 */
import java.util.*;
public class ServerModel {
    private static final String COURSE = "LM051-2026";
    private static final int MAX_MODULES = 5;
    private final HashMap<String, Lecture> roomSched = new HashMap<>();
    private final HashMap<String, Lecture> courseSched = new HashMap<>();
    private final ArrayList<Lecture> lectures = new ArrayList<>();
    private final HashSet<String> modules = new HashSet<>();
    public String getCourse() {
        return COURSE;
    }
    public synchronized String addLecture(String module, String date, String time, String room) {
        if (!modules.contains(module) && modules.size() >= MAX_MODULES) {
            return "FAILURE: Cannot add lecture. Maximum of " + MAX_MODULES + " modules reached";
        
        }
        
        Lecture newLecture = new Lecture(module, date, time, room, COURSE);
        String roomKey = newLecture.getRoomTimeKey();
        if (roomSched.containsKey(roomKey)) {
            return "FAILURE: Room is already booked.";
            
        }
        String courseKey = newLecture.getCourseTimeKey();
        if (courseSched.containsKey(courseKey)) {
            return "FAILURE: " + COURSE + " is already booked at this time.";
        
        }
        
        roomSched.put(roomKey, newLecture);
        courseSched.put(courseKey, newLecture);
        lectures.add(newLecture);
        modules.add(module);
        return "SUCCESS: Lecture added";
    }
    public synchronized String removeLecture(String module, String date, String time, String room) {
        Lecture target = new Lecture(module, date, time, room, COURSE);
        String courseKey = target.getCourseTimeKey();
        if (!courseSched.containsKey(courseKey)) {
            return "FAILURE: No lecture found.";
        }
        Lecture existing = courseSched.get(courseKey);
        if (!existing.getModule().equalsIgnoreCase(module) || !existing.getRoomNumber().equalsIgnoreCase(room)) {
            return "FAILURE: Lecture details do not match for ";
        }
        courseSched.remove(courseKey);
        roomSched.remove(existing.getRoomTimeKey());
        lectures.remove(existing);
        boolean moduleStillExists = false;
            for (Lecture l : lectures) {
        if (l.getModule().equalsIgnoreCase(module)) {
            moduleStillExists = true;
            break;
        }
        }
        if (!moduleStillExists) modules.remove(module);
            return "SUCCESS: Lecture removed.";
    }
    public synchronized String getScheduleSerialized() {
        if (lectures.isEmpty()) {
            return "SCHEDULE|0|No lectures scheduled for " + COURSE + " yet.";
        }
        StringBuilder sb = new StringBuilder("SCHEDULE|" + lectures.size());
        for (Lecture l : lectures) {
            sb.append("|").append(l.serialize());
        }
        return sb.toString();
    }
    public synchronized List<Lecture> getAllLectures() {
        return Collections.unmodifiableList(lectures);
    }
}
