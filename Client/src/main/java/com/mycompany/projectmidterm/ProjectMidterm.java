/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.projectmidterm;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author fabia
 */
public class ProjectMidterm extends Application {
    private ComboBox<String> actionBox;
    private DatePicker datePicker;
    private ComboBox<String> timeBox;
    private TextField roomField;
    private TextField moduleField;
    private Button sendButton;
    private Button stopButton;
    private Button clearButton;
    private TextArea logArea;
    private TableView<Lecture> table;
    private Label statusLabel;
    private javafx.collections.ObservableList<Lecture> schedule =
        javafx.collections.FXCollections.observableArrayList();
    
    @Override 
    public void start(Stage stage) {
        actionBox = new ComboBox();
        actionBox.getItems().addAll("ADD", "REMOVE", "DISPLAY", "OTHER");
        
        datePicker = new DatePicker();
        timeBox = new ComboBox<>();
        timeBox.getItems().addAll("09:00-10:00","10:00-11:00", "11:00-12:00");
        
        roomField = new TextField();
        moduleField = new TextField();
        
        sendButton = new Button("Send Request");
        clearButton = new Button("Clear");
        stopButton = new Button("STOP");
        
        statusLabel = new Label("Connected");
        logArea = new TextArea();
        
        
        table = new TableView<>();
        TableColumn<Lecture, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Lecture, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<Lecture, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        TableColumn<Lecture, String> moduleCol = new TableColumn<>("Module");
        moduleCol.setCellValueFactory(new PropertyValueFactory<>("module"));
        
        table.getColumns().addAll(dateCol,timeCol,roomCol,moduleCol);
        
        GridPane inputPane = new GridPane();
        inputPane.setHgap(5);
        inputPane.setVgap(5);
        inputPane.add(new Label("Action:"), 0,0);
        inputPane.add(actionBox,1,0);
        inputPane.add(new Label("Date"),0,1);
        inputPane.add(datePicker,1,1);
        inputPane.add(new Label("Time"),0,2);
        inputPane.add(timeBox,1,2);
        inputPane.add(new Label("Room"), 0,3);
        inputPane.add(roomField, 1,3);
        inputPane.add(new Label("Module"),0,4);
        inputPane.add(moduleField,1,4);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(sendButton,clearButton,stopButton);
        
        VBox bottomBox = new VBox(10);
        bottomBox.getChildren().addAll(buttonBox,logArea,statusLabel);
        
        BorderPane root = new BorderPane();
        root.setTop(inputPane);
        root.setCenter(table);
        root.setBottom(bottomBox);
        
        sendButton.setOnAction(e -> {
            
            String action = actionBox.getValue();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String time = timeBox.getValue() != null ? timeBox.getValue() : "";
            String room = roomField.getText();
            String module = moduleField.getText();
            
            String request = action + "|" + date + "|" + time +"|" + room + "|" + module;
            
            
            logArea.appendText("CLIENT: " + request + "\n") ;
            String response = server(request);
            logArea.appendText("SERVER: " + response + "\n\n");
            
            if (action.equals("DISPLAY")) {
                table.setItems(schedule);
            }
            
            
            
        });
        
        clearButton.setOnAction(e -> {
            actionBox.setValue(null);
            datePicker.setValue(null);
            timeBox.setValue(null);
            roomField.clear();
            moduleField.clear();
            logArea.appendText("Fields cleared.\n");
        });
        
        stopButton.setOnAction(e -> {
            sendButton.setDisable(true);
            statusLabel.setText("Connection closed.");
            logArea.appendText("STOP pressed.\n");
        });
        
        
        
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Lab3HardLab");
        stage.setScene(scene);
        stage.show();
        
               
    }
public static class Lecture {
    private String date;
    private String time;
    private String room;
    private String module;
    
    public Lecture(String date, String time, String room, String module) {
        this.date = date;
        this.time = time;
        this.room = room;
        this.module = module;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getRoom() {
        return room;
    }
    public String getModule() {
        return module;
    }
    
}
private String server(String request) {
    String[] parts = request.split("\\|", -1);
    String action = parts[0];
    if (action.equals("DISPLAY")) {
        return "Schedule Displayed";
    }
    String date = parts[1];
    String time = parts[2];
    String room = parts[3];
    String module = parts[4];
    
    switch (action) {
        
        case "ADD":
            if (date.isEmpty() || time.isEmpty() || room.isEmpty() || module.isEmpty()) {
                return "ERROR: Missing Fields.";
        }
            for (Lecture lec : schedule) {
                if(lec.getDate().equals(date) && lec.getTime().equals(time)) {
                    return "ERROR: Time slot already booked.";
                }
            }
            schedule.add(new Lecture(date,time,room,module));
            return "SUCCESS: Lecture added.";
            
        case "REMOVE":
            for (Lecture lec : schedule) {
                if (lec.getDate().equals(date) && lec.getTime().equals(time)) {
                    schedule.remove(lec);
                    return "SUCCESS: Lecture removed.";
                }
            }
            return "ERROR: Lecture not found.";
            
        case "OTHER":
            return "Other request received."; 
        
        default:
            return "Unknown";
            
    }
 }
    public static void main(String[] args) {
        launch();
    }
}
