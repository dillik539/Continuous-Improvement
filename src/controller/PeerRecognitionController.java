package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Database;
import util.ToastUtils;

public class PeerRecognitionController {
	
	private Stage recognitionStage;
	private TextField recognizedUserField, recognizerField, productLineField;
	private DatePicker datePicker;
	private TextArea recognitionText;
	private CheckBox fosterBelonging, moveWithSpeed, competeToWin, actBoldly, deliverResults;
	private Label statusLabel;
	
	private static final double FIELD_WIDTH = 220; //For consistent width of all input fields
	
	
	public PeerRecognitionController () {
		recognitionStage = new Stage();
		recognitionStage.initModality(Modality.APPLICATION_MODAL);
		recognitionStage.setTitle("Peer Recognition");
		
		double labelWidth = 200;
		
		recognizedUserField = new TextField();
		recognizedUserField.setPromptText("Enter name");
		recognizedUserField.setPrefWidth(FIELD_WIDTH);
		
		recognizerField = new TextField();
		recognizerField.setPromptText("Your Name");
		recognizerField.setPrefWidth(FIELD_WIDTH);
		
		productLineField = new TextField();
		productLineField.setPromptText("Product Line");
		productLineField.setPrefWidth(FIELD_WIDTH);
		
		datePicker = new DatePicker(LocalDate.now());
		datePicker.setPrefWidth(FIELD_WIDTH);
		
		//Left section
		VBox leftSection = new VBox(12,
		labeledField("WHO is being Recognied?", recognizedUserField),
		labeledField("WHO is filling out this card?", recognizerField),
		labeledField("Product Line: ", productLineField),
		labeledField("DATE: ", datePicker)
		);
		leftSection.setAlignment(Pos.TOP_LEFT);
		
		
		Label mindsetTitle = new Label("MEDTRONIC MINDSET");
		mindsetTitle.setStyle("-fx-font-weight: bold; -fx-underline: true;");
		
		//checkboxes - right section
		
		fosterBelonging = new CheckBox("Foster Belonging");
		moveWithSpeed = new CheckBox("Move with Speed and Decisiveness");
		competeToWin = new CheckBox("Compete to Win");
		actBoldly = new CheckBox("Act Boldly");
		deliverResults = new CheckBox("Deliver Results the Right way");
		
		VBox mindsetBox = new VBox(8, mindsetTitle, fosterBelonging, moveWithSpeed, competeToWin, actBoldly, deliverResults);
		mindsetBox.setPadding(new Insets(10));
		mindsetBox.setStyle("-fx-border-color: #aaccee; -fx-padding: 8; -fx-background-color: #f9fbff;");
		mindsetBox.setPrefWidth(280);
		
		//combine left and right section to align them horizontally
		HBox topSection = new HBox(30, leftSection, mindsetBox);
		topSection.setAlignment(Pos.TOP_LEFT);
		
		//Bottom section- recognition text
		Label recognitionLabel = new Label("WHAT is the Operator being recognized for?");
		recognitionLabel.setStyle("-fx-font-weight: bold;");
		
		recognitionText = new TextArea();
		recognitionText.setPromptText("Describe the Contribution...");
		recognitionText.setPrefHeight(120);
		recognitionText.setWrapText(true);
		
		VBox bottomSection = new VBox(6, recognitionLabel, recognitionText);
		bottomSection.setPadding(new Insets(10,0,0,0));

		
		//Buttons
        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        submitButton.setPrefWidth(120);
        cancelButton.setPrefWidth(120);

        submitButton.getStyleClass().add("action-button");
        cancelButton.getStyleClass().add("action-button");

        submitButton.setOnAction(e -> submitRecognition());
        cancelButton.setOnAction(e -> recognitionStage.close());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBox = new HBox(10, spacer, submitButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        //Status Label
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-style: italic;");
        VBox.setMargin(statusLabel, new Insets(0, 0, 5, 0));

        // Main Layout
        VBox layout = new VBox(15, topSection, bottomSection, buttonBox, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #ffffff;");

        //Scene
        Scene scene = new Scene(layout, 600, 480);
        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());

        recognitionStage.setScene(scene);
    }
	
	private HBox labeledField(String labelText, javafx.scene.Node field) {
        Label label = new Label(labelText);
        label.setPrefWidth(200);
        HBox box = new HBox(10, label, field);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

	private void submitRecognition() {
	    String recognizee = recognizedUserField.getText().trim();
	    String recognizer = recognizerField.getText().trim();
	    String product = productLineField.getText().trim();
	    LocalDate date = datePicker.getValue();
	    String text = recognitionText.getText().trim();

	    List<String> mindsets = new ArrayList<>();
	    if (fosterBelonging.isSelected()) mindsets.add("Foster Belonging");
	    if (moveWithSpeed.isSelected()) mindsets.add("Move with Speed and Decisiveness");
	    if (competeToWin.isSelected()) mindsets.add("Compete to Win");
	    if (actBoldly.isSelected()) mindsets.add("Act Boldly");
	    if (deliverResults.isSelected()) mindsets.add("Deliver Results the Right Way");

	    if (recognizee.isEmpty() || recognizer.isEmpty() || product.isEmpty() || text.isEmpty() || mindsets.isEmpty()) {
	        ToastUtils.showToast(recognitionStage.getScene(), "Please complete all fields.");
	        return;
	    }

	    try (Connection conn = Database.getConnection()) {
	        PreparedStatement stmt = conn.prepareStatement(
	            "INSERT INTO peer_recognitions (recognized_user, recognized_by, product_line, recognition_date, mindset, recognition_text) VALUES (?, ?, ?, ?, ?, ?)"
	        );
	        stmt.setString(1, recognizee);
	        stmt.setString(2, recognizer);
	        stmt.setString(3, product);
	        stmt.setDate(4, java.sql.Date.valueOf(date));
	        stmt.setString(5, String.join(", ", mindsets));
	        stmt.setString(6, text);
	        stmt.executeUpdate();

	        ToastUtils.showToast(recognitionStage.getScene(), "Recognition submitted successfully!");
	        clearFields();
	    } catch (Exception e) {
	        e.printStackTrace();
	        ToastUtils.showToast(recognitionStage.getScene(), "Error submitting recognition.");
	    }
	}


    private void clearFields() {
        recognizedUserField.clear();
        recognizerField.clear();
        productLineField.clear();
        datePicker.setValue(LocalDate.now());
        recognitionText.clear();
        fosterBelonging.setSelected(false);
        moveWithSpeed.setSelected(false);
        competeToWin.setSelected(false);
        actBoldly.setSelected(false);
        deliverResults.setSelected(false);
    }

    public void show() {
        recognitionStage.showAndWait();
    }

	}
	

