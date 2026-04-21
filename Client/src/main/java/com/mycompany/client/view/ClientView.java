/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.*;

import javafx.geometry.Pos;
import java.time.DayOfWeek;
import java.util.*;

/**
 *
 * @author b4zel
 */
public class ClientView {

    public ComboBox<String> actionBox;
    private DatePicker datePicker;
    private ComboBox<String> timeSlot;
    private TextField roomNum;
    private TextField moduleNum;

    public Button sendBtn;
    public Button stopBtn;
    public Button clearBtn;
    public Button connectBtn;

    public TextArea readOnly;
    public Label statusLabel;

 
    private GridPane timetableGrid;
    private static final String[] SHORT_DAYS = {"Mon", "Tues", "Wed", "Thur", "Fri"};
    private static final String[] timeSlots = {"09:00-10:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "16:00-17:00", "17:00-18:00"};
    
    // maybe add an option to switch from AM/PM time to Military time
    
    
    VBox lectureInputPanel;

    private final Stage stage;
    private boolean darkMode = false;
    private Scene scene;
    private HBox header;
    private VBox bottomPanel;
    
    private Label titleLabel;
    private Label gridTitleLabel;
    private Label logLabel;
    private Button darkToggleBtn;
    
    public ClientView(Stage stage) {
        this.stage = stage;
    }

    public void start() {
       
        actionBox = new ComboBox<>();
        actionBox.getItems().addAll("ADD", "REMOVE", "DISPLAY", "EARLY", "OTHER");
        actionBox.setValue("ADD");

        datePicker = new DatePicker(LocalDate.now());
        timeSlot = new ComboBox<>();
        timeSlot.getItems().addAll(timeSlots);
       
        moduleNum = new TextField();
        moduleNum.setPromptText("e.g. CS4076");

        roomNum = new TextField();
        roomNum.setPromptText("e.g. CS-101");

        sendBtn    = new Button("Send Request");
        stopBtn    = new Button("STOP");
        clearBtn   = new Button("CLEAR");
        connectBtn = new Button("Connect");

        sendBtn.setDisable(true);
        stopBtn.setDisable(true);

        readOnly = new TextArea();
        readOnly.setEditable(false);
        readOnly.setWrapText(true);
        

        statusLabel = new Label("Status: Not connected");

        lectureInputPanel = new VBox(6, new Label("Module:"), moduleNum,
                new Label("Date:"), datePicker,
                new Label("Time:"), timeSlot,
                new Label("Room:"), roomNum);

        lectureInputPanel.setVisible(false);
        lectureInputPanel.setManaged(false);
        showLectureInputPanel(true);
        
        

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(8));
        grid.add(new Label("Action:"), 0, 0);
        grid.add(actionBox, 1, 0);

        grid.add(lectureInputPanel, 0, 1, 2, 1);
        VBox buttonBox = new VBox(10, connectBtn, sendBtn, clearBtn, stopBtn);
        buttonBox.setPadding(new Insets(10,0,0,0));

        connectBtn.setMaxWidth(Double.MAX_VALUE);
        sendBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        stopBtn.setMaxWidth(Double.MAX_VALUE);
        
        

        BorderPane border = new BorderPane();
        titleLabel = new Label("Lecture Scheduler Client");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        darkToggleBtn = new Button("Dark Mode");
        darkToggleBtn.setOnAction(e -> toggleDarkMode());
        header = new HBox(10, titleLabel, darkToggleBtn);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color:#f2f2f2");
        
        timetableGrid = new GridPane();
        timetableGrid.setHgap(2);
        timetableGrid.setVgap(2);
        timetableGrid.setPadding(new Insets(4));
        buildEmptyGrid();
        
        HBox.setHgrow(timetableGrid, Priority.ALWAYS);
        ScrollPane scrollPane = new ScrollPane(timetableGrid);
        scrollPane.setFitToWidth(true);
        gridTitleLabel = new Label("Weekly Timetable"); // maybe add an option to change between timetables for different courses 
        gridTitleLabel.setStyle("-fx-font-weight: bold;");
        VBox centerBox = new VBox(8, gridTitleLabel, scrollPane);
        centerBox.setPadding(new Insets(12));
        border.setCenter(centerBox);
        
        border.setTop(header);
        VBox leftPanel = new VBox(10, new Label("Request Builder"), grid, buttonBox);
        leftPanel.setPadding(new Insets(12));
        leftPanel.setPrefWidth(300);
        leftPanel.setStyle("-fx-border-color:#dddddd; -fx-border-width: 0 1 0 0");
        border.setLeft(leftPanel);
        logLabel = new Label("Conversation Log Client-Server");
        bottomPanel = new VBox(6, logLabel, readOnly, statusLabel);
        
    bottomPanel.setPadding(new Insets(12));
    bottomPanel.setStyle("-fx-background-color:#fafafa; -fx-border-color:#dddddd; -fx-border-width:1 0 0 0");

    border.setBottom(bottomPanel);
        
        scene = new Scene(border, 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Lecture Scheduler Client");
        stage.show();
    }
    private void toggleDarkMode() {
    darkMode = !darkMode;
    if (darkMode) {
        scene.getRoot().setStyle("-fx-base: #3c3f41;");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        gridTitleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        logLabel.setStyle("-fx-text-fill: white;");
        statusLabel.setStyle("-fx-text-fill: white;");
        darkToggleBtn.setText("Light Mode");
        header.setStyle("-fx-background-color: #2b2b2b;");
        bottomPanel.setStyle("-fx-background-color: #2b2b2b; -fx-border-color:#555555; -fx-border-width:1 0 0 0");
    } else {
        scene.getRoot().setStyle("");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
        gridTitleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        logLabel.setStyle("-fx-text-fill: black;");
        statusLabel.setStyle("-fx-text-fill: black;");
        darkToggleBtn.setText("Dark Mode");
        header.setStyle("-fx-background-color: #f2f2f2;");
        bottomPanel.setStyle("-fx-background-color:#fafafa; -fx-border-color:#dddddd; -fx-border-width:1 0 0 0");
    }
    buildEmptyGrid();
    
}
   
    public void updateSchedule(String response) {
        buildEmptyGrid();
        if (response == null || !response.startsWith("SCHEDULE|")) return;
        String[] parts = response.split("\\|");
        int count;
        try { count = Integer.parseInt(parts[1]); } catch (NumberFormatException e) { return; }
        for (int i = 0; i < count; i++) {
            int base = 2 + i * 5;
            if (base + 3 >= parts.length) continue;
            String module = parts[base];
            String date   = parts[base + 1];
            String time   = parts[base + 2];
            String room   = parts[base + 3];
            int col = getDayColumn(date);
            int row = getTimeRow(time);
            if (col < 0 || row < 0) continue;
            final int fc = col, fr = row + 1;
            timetableGrid.getChildren().removeIf(n -> {
                Integer c = GridPane.getColumnIndex(n);
                Integer r = GridPane.getRowIndex(n);
                return c != null && r != null && c == fc && r == fr;
            });
            timetableGrid.add(makeLectureCell(module, room), col, row + 1);
        }
    }

    private void buildEmptyGrid() {
        timetableGrid.getChildren().clear();
        timetableGrid.getColumnConstraints().clear();
        timetableGrid.getRowConstraints().clear();
        timetableGrid.add(makeHeaderCell("Time / Day"), 0, 0);
        for (int d = 0; d < SHORT_DAYS.length; d++) {
            timetableGrid.add(makeHeaderCell(SHORT_DAYS[d]), d + 1, 0);
        }
        for (int t = 0; t < timeSlots.length; t++) {
            timetableGrid.add(makeTimeCell(timeSlots[t]), 0, t + 1);
            for (int d = 0; d < SHORT_DAYS.length; d++) {
                timetableGrid.add(makeEmptyCell(), d + 1, t + 1);
            }
        }
        timetableGrid.getColumnConstraints().add(new ColumnConstraints(110));
        for (int d = 0; d < SHORT_DAYS.length; d++) {
            ColumnConstraints cc = new ColumnConstraints(100, 150, Double.MAX_VALUE);
            cc.setHgrow(Priority.ALWAYS);
            timetableGrid.getColumnConstraints().add(cc);
        }
        timetableGrid.getRowConstraints().add(new RowConstraints(36));
        for (int t = 0; t < timeSlots.length; t++) {
            timetableGrid.getRowConstraints().add(new RowConstraints(60));
        }
    }

     private Label makeHeaderCell(String text) {
        Label lbl = new Label(text);
        String bg = darkMode ? "#2b2b2b" : "#3a3a3a";
        lbl.setStyle("-fx-font-weight: bold; -fx-background-color: " + bg + "; -fx-padding: 6; -fx-text-fill: white;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setMinHeight(36);
        return lbl;
    }

    private Label makeTimeCell(String time) {
        Label lbl = new Label(time);
        String bg = darkMode ? "#45494a" : "#f0f0f0";
        String fg = darkMode ? "white"   : "#333333";
        lbl.setStyle("-fx-background-color: " + bg + "; -fx-padding: 4; -fx-font-size: 11px; -fx-text-fill: " + fg + "; -fx-border-color: #555555; -fx-border-width: 0.5;");
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setMinHeight(60);
        return lbl;
    }

     private Label makeEmptyCell() {
        Label lbl = new Label("");
        String bg = darkMode ? "#3c3f41" : "#ffffff";
        lbl.setStyle("-fx-background-color: " + bg + "; -fx-border-color: #555555; -fx-border-width: 0.5;");
        lbl.setMinHeight(60);
        lbl.setMaxWidth(Double.MAX_VALUE);
        return lbl;
    }

     private VBox makeLectureCell(String module, String room) {
        Label modLbl  = new Label(module);
        modLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        Label roomLbl = new Label(room);
        roomLbl.setStyle("-fx-font-size: 11px;");
        VBox cell = new VBox(2, modLbl, roomLbl);
        cell.setPadding(new Insets(4, 6, 4, 6));
        String bg = darkMode ? "#1e4d78" : "#d0e8ff";
        cell.setStyle("-fx-background-color: " + bg + "; -fx-border-color: #555555; -fx-border-width: 0.5;");
        cell.setMinHeight(60);
        cell.setMaxWidth(Double.MAX_VALUE);
        return cell;
    }

    private int getDayColumn(String dateStr) {
        try {
            LocalDate d = LocalDate.parse(dateStr);
            switch (d.getDayOfWeek()) {
                case MONDAY:    return 1;
                case TUESDAY:   return 2;
                case WEDNESDAY: return 3;
                case THURSDAY:  return 4;
                case FRIDAY:    return 5;
                default:        return -1;
            }
        } catch (Exception e) { return -1; }
    }

    private int getTimeRow(String time) {
        for (int i = 0; i < timeSlots.length; i++) {
            if (timeSlots[i].equals(time)) return i;
        }
        return -1;
    }

    public void clearInputs() {
        moduleNum.clear();
        roomNum.clear();
        datePicker.setValue(LocalDate.now());
        timeSlot.setValue(null);
        actionBox.setValue("ADD");
        showLectureInputPanel(true);
    }
    
    public void setStatus(String msg){ statusLabel.setText("Status: " + msg); }
    public void appendResponse(String msg) {
    if (msg.startsWith("[Sent]"))
        readOnly.appendText(">> " + msg.substring(7) + "\n");
    else if(msg.startsWith("[Received]")) 
        readOnly.appendText("<< " + msg.substring(11) + "\n");
    else  
        readOnly.appendText(msg + "\n");
    }
    public void clearResponse(){ readOnly.clear(); }

    public String getSelectedAction(){ return actionBox.getValue(); }
    public String getSelectedModule(){ return moduleNum.getText().trim(); }
    public String getSelectedTime(){ return timeSlot.getValue(); }
    public String getRoomNumber(){return roomNum.getText().trim(); }
    public String getSelectedDate(){ return datePicker.getValue() != null ? datePicker.getValue().toString() : ""; }

    public void showLectureInputPanel(boolean show) {
        lectureInputPanel.setVisible(show);
        lectureInputPanel.setManaged(show);
    }
}