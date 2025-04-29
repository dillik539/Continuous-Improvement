package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Database;
import model.Idea;

public class IdeaController {
	private String username;
	private VBox layout;
	private TextField shortDescriptionField;
	private TextArea ideaArea;
	private TableView<Idea> ideaTable;
	private Label lastRefreshedLabel;
	private Button refreshButton;
	private int previousIdeaCount = 0;

	public IdeaController(String username) {
		this.username = username;
		layout = new VBox(10);

		shortDescriptionField = new TextField();
		ideaArea = new TextArea();
		
		lastRefreshedLabel = new Label();
		refreshButton = new Button("Refresh Now");
		refreshButton.setOnAction(e -> loadIdeas());

		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e -> submitIdea());

		ideaTable = new TableView<>();
		setupTable();

		layout.getChildren().addAll(new Label("Short Description"), shortDescriptionField, new Label("Your Idea"),
				ideaArea, submitButton,refreshButton, lastRefreshedLabel, new Label("Your Ideas"), ideaTable);
		
		loadIdeas();//Load ideas initailly from the database
		startAutoRefresh(); //refresh loaded data periodically to reflect all ideas added including the recentones from the database
	
	}

	private void setupTable() {
		TableColumn<Idea, Integer> userIdCol = new TableColumn<>("User ID");
		//asObject() wraps the primitive int property returned by getUserID() to make it an Integer object.
		userIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserID()).asObject());
		TableColumn<Idea, String> shortDescCol = new TableColumn<>("Short Description");
		shortDescCol
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShortDescription()));

		TableColumn<Idea, String> descCol = new TableColumn<>("Idea");
		descCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullIdea()));

		TableColumn<Idea, String> dateCol = new TableColumn<>("Date Submitted");
		dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateSubmitted()));

		TableColumn<Idea, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

		ideaTable.getColumns().addAll(userIdCol,shortDescCol, descCol, dateCol, statusCol);
	}

	private void submitIdea() {
		
		String shortDesc = shortDescriptionField.getText().trim();
		String ideaText = ideaArea.getText().trim();

		
		if(shortDesc.isEmpty() && ideaText.isEmpty()) {
			showAlert("Submission Error", "Both 'Short Description' and 'Your Idea' cannot be empty!");
			return;
		}
		if(shortDesc.isEmpty()) {
			showAlert("Submission Error", "Short Description cannot be empty!");
			return;
		}
		if(ideaText.isEmpty()) {
			showAlert("Submission Error", "Your Idea field cannot be empty!");
			return;
		}
		int userId = findUserId(username);
		// get the current date and time in format: yyyy-MM-ddTHH:mm:ss. T = time
		LocalDateTime now = LocalDateTime.now();

		/*
			* change dateTime in the specified format of type String.Format pattern
			* yyyy-MM-dd HH:mm:ss.
		*/
		String formattedCurrentDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO ideas (user_id, short_description, idea_text, date_submitted, status) VALUES (?, ?, ?, ?, ?)");
			stmt.setInt(1, userId);
			stmt.setString(2, shortDesc);
			stmt.setString(3, ideaText);
			stmt.setString(4, formattedCurrentDateTime);
			stmt.setString(5, "Pending");
			stmt.executeUpdate();
			clearFields(); //clear all fields to prevent submitting duplicate ideas.
			loadIdeas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadIdeas() {
		/**
		 * ObservableList<Idea> ideas = FXCollections.observableArrayList(); try
		 * (Connection conn = Database.getConnection()) { // get user id from the users
		 * table that can be used to obtain associated ideas String userIdQuery =
		 * "SELECT id FROM users WHERE username = ?"; PreparedStatement userIdStmt =
		 * conn.prepareStatement(userIdQuery); userIdStmt.setString(1, username);
		 * ResultSet userIdRs = userIdStmt.executeQuery();
		 * 
		 * int userId = findUserId(username); if (userIdRs.next()) { userId =
		 * userIdRs.getInt("id"); } else { System.out.println("User not found in
		 * database"); return; // Exit if user not found. }
		 **/
		ObservableList<Idea> ideas = FXCollections.observableArrayList();
		try (Connection conn = Database.getConnection()) {
			String ideaQuery = "SELECT user_id, short_description, idea_text, date_submitted, status FROM ideas WHERE user_id= ?";
			int userId = findUserId(username);
			PreparedStatement stmt = conn.prepareStatement(ideaQuery);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ideas.add(new Idea(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		ideaTable.setItems(ideas);
		lastRefreshedLabel.setText("Last Refreshed at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		
		//Detect if any new ideas are added
		if(ideas.size() > previousIdeaCount) {
			showNewIdeaNotification(ideas.size() - previousIdeaCount);
		}
		previousIdeaCount = ideas.size();

	}

	public VBox getView() {
		return layout;
	}

	private int findUserId(String username) {
		int userId = 0;
		try (Connection conn = Database.getConnection()) {
			// get user id from the users table that can be used to obtain associated ideas
			String userIdQuery = "SELECT id FROM users WHERE username = ?";
			PreparedStatement userIdStmt = conn.prepareStatement(userIdQuery);
			userIdStmt.setString(1, username);
			ResultSet userIdRs = userIdStmt.executeQuery();

			if (userIdRs.next()) {
				userId = userIdRs.getInt("id");
			} else {
				System.out.println("User not found in database");
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}
	private void startAutoRefresh() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> loadIdeas()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	private void clearFields() {
		shortDescriptionField.clear();
		ideaArea.clear();
	}
	
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	private void showNewIdeaNotification(int newCount) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("New Idea Detected");
		alert.setHeaderText(null);
		alert.setContentText("There are " + newCount + " new idea(s) since the last refresh.");
		alert.show();
	}
}
