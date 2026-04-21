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
import java.util.concurrent.ForkJoinPool;
public class ServerModel {
    private static final String COURSE = "LM051-2026";
    private static final int MAX_MODULES = 5;
    private final HashMap<String, Lecture> roomSched = new HashMap<>();
    private final HashMap<String, Lecture> courseSched = new HashMap<>();
    private final ArrayList<Lecture> lectures = new ArrayList<>();
    private final HashSet<String> modules = new HashSet<>();

    private static final String[] TIME_SLOTS = {
        "09:00-10:00", "11:00-12:00", "12:00-13:00",
        "13:00-14:00", "14:00-15:00", "16:00-17:00", "17:00-18:00"
    };
    private static final int EARLY_CUTOFF = 3;

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

    public synchronized Set<String> getUniqueDates() {
        Set<String> dates = new HashSet<>();
        for (Lecture l : lectures) dates.add(l.getDate());
        return dates;
    }

    private int getTimeIndex(String time) {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(time)) return i;
        }
        return -1;
    }

    public synchronized String shiftDayEarly(String date) {
        List<Lecture> dayLectures = new ArrayList<>();
        for (Lecture l : lectures) {
            if (l.getDate().equals(date)) dayLectures.add(l);
        }
        Set<Integer> occupiedEarly = new HashSet<>();
        for (Lecture l : dayLectures) {
            int idx = getTimeIndex(l.getTime());
            if (idx >= 0 && idx < EARLY_CUTOFF) occupiedEarly.add(idx);
        }
        List<Lecture> lateLectures = new ArrayList<>();
        for (Lecture l : dayLectures) {
            int idx = getTimeIndex(l.getTime());
            if (idx >= EARLY_CUTOFF) lateLectures.add(l);
        }
        if (lateLectures.isEmpty()) return "No late lectures to shift on " + date;
        int shifted = 0;
        for (Lecture late : lateLectures) {
            for (int i = 0; i < EARLY_CUTOFF; i++) {
                if (occupiedEarly.contains(i)) continue;
                String newTime = TIME_SLOTS[i];
                String newRoomKey = date.toUpperCase() + "|" + newTime + "|" + late.getRoomNumber().toUpperCase();
                if (roomSched.containsKey(newRoomKey)) continue;
                courseSched.remove(late.getCourseTimeKey());
                roomSched.remove(late.getRoomTimeKey());
                lectures.remove(late);
                Lecture moved = new Lecture(late.getModule(), date, newTime, late.getRoomNumber(), late.getCourse());
                courseSched.put(moved.getCourseTimeKey(), moved);
                roomSched.put(moved.getRoomTimeKey(), moved);
                lectures.add(moved);
                occupiedEarly.add(i);
                shifted++;
                break;
            }
        }
        return shifted + " lecture(s) shifted early on " + date;
    }

    public String processEarlyLectures() {
        Set<String> dates = getUniqueDates();
        if (dates.isEmpty()) return "EARLY|FAILURE: No lectures scheduled.";
        List<String> dateList = new ArrayList<>(dates);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new EarlyLectureTask(dateList, this));
        return "EARLY|SUCCESS: Early lectures processed for " + dateList.size() + " day(s).";
    }
}