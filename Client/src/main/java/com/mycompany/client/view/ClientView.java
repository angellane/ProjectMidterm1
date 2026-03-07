/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

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

 
    private TableView<String[]> scheduleTable;
    private final ObservableList<String[]> scheduleData = FXCollections.observableArrayList();

    VBox lectureInputPanel;

    private final Stage stage;

    public ClientView(Stage stage) {
        this.stage = stage;
    }

    public void start() {
       
      
       
        actionBox = new ComboBox<>();
        actionBox.getItems().addAll("ADD", "REMOVE", "DISPLAY", "OTHER");
        actionBox.setValue("ADD");

        datePicker = new DatePicker(LocalDate.now());
        timeSlot = new ComboBox<>();
        timeSlot.getItems().addAll("09:00-10:00",
               "11:00-12:00",
               "12:00-13:00",
               "13:00-14:00",
               "14:00-15:00",
               "16:00-17:00",
               "17:00-18:00");
       
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
        
        scheduleTable = new TableView<>(scheduleData);
        String[] columns = {"Module", "Date", "Time", "Room"};
        for (int i = 0; i < columns.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(columns[i]);
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[idx]));
            scheduleTable.getColumns().add(col);
        }

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
        Label title = new Label("Timetable Client");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox header = new HBox(title);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color:#f2f2f2");
        
        border.setTop(header);
        border.setCenter(scheduleTable);
        VBox leftPanel = new VBox(10, new Label("Request Builder"), grid, buttonBox);
        leftPanel.setPadding(new Insets(12));
        leftPanel.setPrefWidth(300);
        leftPanel.setStyle("-fx-border-color:#dddddd; -fx-border-width: 0 1 0 0");
        border.setLeft(leftPanel);
        VBox bottomPanel = new VBox(6,new Label("Conversation Log"),readOnly,statusLabel
);

    bottomPanel.setPadding(new Insets(12));
    bottomPanel.setStyle("-fx-background-color:#fafafa; -fx-border-color:#dddddd; -fx-border-width:1 0 0 0");

    border.setBottom(bottomPanel);
        
        stage.setScene(new Scene(border, 1000, 800));
        stage.setTitle("Lecture Scheduler Client");
        stage.show();
    }

   
    public void updateSchedule(String response) {
        scheduleData.clear();
        if (response == null || !response.startsWith("SCHEDULE|")) return;
        String[] parts = response.split("\\|");
        int count;
        try { count = Integer.parseInt(parts[1]); } catch (NumberFormatException e) { return; }
        for (int i = 0; i < count; i++) {
    int base = 2 + i * 5;
    if (base + 3 <= parts.length) {
        String[] row = { parts[base], parts[base+1], parts[base+2], parts[base+3], };
    
        scheduleData.add(row);
    }
}
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