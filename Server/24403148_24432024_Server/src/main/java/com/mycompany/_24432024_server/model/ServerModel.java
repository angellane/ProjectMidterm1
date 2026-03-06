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
    private final HashMap<String, Lecture> roomSchedule = new HashMap<>();
    private final HashMap<String, Lecture> courseSchedule = new HashMap<>();
    private final ArrayList<Lecture> allLectures = new ArrayList<>();
    private final HashSet<String> modules = new HashSet<>();
    public String getCourse() {
        return COURSE;
    }
    public String addLecture(String module, String date, String time, String room) {
        if (!modules.contains(module) && modules.size() >= MAX_MODULES) {
            return "FAILURE: Cannot add lecture. Maximum of " + MAX_MODULES + " modules reached for course " + COURSE + ".";
        }
        Lecture newLecture = new Lecture(module, date, time, room, COURSE);
        String roomKey = newLecture.getRoomTimeKey();
        if (roomSchedule.containsKey(roomKey)) {
            Lecture clash = roomSchedule.get(roomKey);
            return "FAILURE: Room clash! Room " + room + " is already booked on " + date +
                   " at " + time + " for module " + clash.getModule() + " (" + clash.getCourse() + ").";
        }
        String courseKey = newLecture.getCourseTimeKey();
        if (courseSchedule.containsKey(courseKey)) {
            Lecture clash = courseSchedule.get(courseKey);
            return "FAILURE: Schedule clash! Course " + COURSE + " already has a lecture on " + date +
                   " at " + time + " for module " + clash.getModule() + " in room " + clash.getRoomNumber() + ".";
        }
        roomSchedule.put(roomKey, newLecture);
        courseSchedule.put(courseKey, newLecture);
        allLectures.add(newLecture);
        modules.add(module);
        return "SUCCESS: Lecture for " + module + " (" + COURSE + ") scheduled on " + date +
               " at " + time + " in room " + room + ".";
    }
    public String removeLecture(String module, String date, String time, String room) {
        Lecture target = new Lecture(module, date, time, room, COURSE);
        String courseKey = target.getCourseTimeKey();
        if (!courseSchedule.containsKey(courseKey)) {
            return "FAILURE: No lecture found for " + module + " on " + date + " at " + time + " in room " + room + ".";
        }
        Lecture existing = courseSchedule.get(courseKey);
        if (!existing.getModule().equalsIgnoreCase(module) || !existing.getRoomNumber().equalsIgnoreCase(room)) {
            return "FAILURE: Lecture details do not match for " + module + " on " + date + " at " + time + ".";
        }
        courseSchedule.remove(courseKey);
        roomSchedule.remove(existing.getRoomTimeKey());
        allLectures.remove(existing);
        boolean moduleStillExists = allLectures.stream()
            .anyMatch(l -> l.getModule().equalsIgnoreCase(module));
        if (!moduleStillExists) {
            modules.remove(module);
        }
        return "SUCCESS: Lecture for " + module + " removed. Time slot " + date + " " + time +
               " in room " + room + " is now free.";
    }
    public String getScheduleSerialized() {
        if (allLectures.isEmpty()) {
            return "SCHEDULE|0|No lectures scheduled for " + COURSE + " yet.";
        }
        StringBuilder sb = new StringBuilder("SCHEDULE|" + allLectures.size());
        for (Lecture l : allLectures) {
            sb.append("|").append(l.serialize());
        }
        return sb.toString();
    }
    public List<Lecture> getAllLectures() {
        return Collections.unmodifiableList(allLectures);
    }
}
