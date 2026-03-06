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

        lectureInputPanel = new VBox(4, new Label("Module:"), moduleNum,
                new Label("Date:"), datePicker,
                new Label("Time:"), timeSlot,
                new Label("Room:"), roomNum);

        
        scheduleTable = new TableView<>(scheduleData);
        String[] columns = {"Module", "Date", "Time", "Room"};
        for (int i = 0; i < columns.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(columns[i]);
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[idx]));
            scheduleTable.getColumns().add(col);
        }

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(8));
        grid.add(new Label("Action:"), 0, 0); grid.add(actionBox,       1, 0);
        grid.add(lectureInputPanel,    0, 1, 2, 1);
        grid.add(connectBtn,           0, 2); grid.add(sendBtn,  1, 2);
        grid.add(clearBtn,             2, 2); grid.add(stopBtn,  3, 2);

        BorderPane border = new BorderPane();
        border.setTop(grid);
        border.setCenter(scheduleTable);
        border.setBottom(new VBox(readOnly, statusLabel));

        stage.setScene(new Scene(border, 800, 600));
        stage.setTitle("Lecture Scheduler Client");
        stage.show();
    }

   
    public void updateSchedule(String response) {
        scheduleData.clear();
        if (response == null || !response.startsWith("SCHEDULE|")) return;
        String[] parts = response.split("\\|");
        int count;
        try { count = Integer.parseInt(parts[1]); } catch (NumberFormatException e) { return; }
        for (int i = 0; i < count && (i + 2) < parts.length; i++) {
            String[] fields = parts[i + 2].split("~");
            if (fields.length >= 4) scheduleData.add(fields);
        }
    }

    public void setStatus(String msg)      { statusLabel.setText("Status: " + msg); }
    public void appendResponse(String msg) { readOnly.appendText(msg + "\n"); }
    public void clearResponse()            { readOnly.clear(); }

    public String getSelectedAction() { return actionBox.getValue(); }
    public String getSelectedModule() { return moduleNum.getText().trim(); }
    public String getSelectedTime()   { return timeSlot.getValue(); }
    public String getRoomNumber()     { return roomNum.getText().trim(); }
    public String getSelectedDate()   { return datePicker.getValue() != null ? datePicker.getValue().toString() : ""; }

    public void showLectureInputPanel(boolean show) {
        lectureInputPanel.setVisible(show);
        lectureInputPanel.setManaged(show);
    }
}