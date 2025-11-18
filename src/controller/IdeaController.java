package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.Database;
import model.Idea;
import util.*;

public class IdeaController {
	private String username;
	private HBox mainLayout;
	private VBox ideaLayout;
	private VBox viewLayout;
	private TextField shortDescriptionField;
	private TextArea ideaArea, ideaBenefitArea, commentArea;
	private TableView<Idea> ideaTable;
	private Label lastRefreshedLabel;
	private Button refreshButton;
	private int previousIdeaCount = 0;

	public IdeaController(String username) {
		this.username = username;
		
		mainLayout = new HBox(20); //space between form (left panel) and table (right panel)
		mainLayout.setPadding(new Insets(10, 20, 10, 20)); //top, right, bottom, left (extra bottom space)
		mainLayout.getStyleClass().add("main-layout"); //root styling
		
		ideaLayout = new VBox(10); //left side (form)
		ideaLayout.setMinWidth(350); //minimum width before shrinking
		ideaLayout.setPrefWidth(400); //preferred width (default space it tries to take)
		ideaLayout.setMaxWidth(500); //maximum width
		HBox.setHgrow(ideaLayout, Priority.ALWAYS);
		VBox.setVgrow(ideaLayout, Priority.ALWAYS);
		ideaLayout.getStyleClass().add("idea-form"); //form styling
		
		viewLayout = new VBox(10); //right side (table and refresh button)
		HBox.setHgrow(viewLayout, Priority.ALWAYS); //stretch right panel
		viewLayout.setMinWidth(400); //minimum width before shrinking
		viewLayout.setPrefWidth(500);
		viewLayout.setMaxWidth(Double.MAX_VALUE);
		viewLayout.getStyleClass().add("idea-table-section"); // right panel styling
		
		//Labels
		//Label shortDescriptionLabel = new Label("Short Description:");
		Label ideaLabel = new Label("Describe Your Idea:");
		Label ideaBenefitLabel = new Label("What are the benifits of your idea?");
		Label commentSuggestionLabel = new Label("Additional comments or suggestions:");
		
		//shortDescriptionField = new TextField();
		//shortDescriptionField.getStyleClass().add("short-description-field"); //text-field styling
		
		ideaArea = new TextArea();//Describe your idea here
		//Let textArea expand vertically
		VBox.setVgrow(ideaArea, Priority.ALWAYS);
		ideaArea.getStyleClass().add("idea-textarea");
		
		ideaBenefitArea = new TextArea(); //what are the benefits of your idea
		VBox.setVgrow(ideaBenefitArea, Priority.ALWAYS);
		ideaBenefitArea.getStyleClass().add("idea-textarea");
		
		commentArea = new TextArea();//Additional comments if you have here(optional)
		VBox.setVgrow(commentArea, Priority.ALWAYS);
		commentArea.getStyleClass().add("idea-textarea");
		
		
		refreshButton = new Button("Refresh Now");
		refreshButton.setOnAction(e -> loadIdeas());
		refreshButton.getStyleClass().add("action-button");
		
		lastRefreshedLabel = new Label("Last Refreshed at : --");
		HBox.setHgrow(lastRefreshedLabel, Priority.ALWAYS);
		lastRefreshedLabel.setMaxWidth(Double.MAX_VALUE);
		lastRefreshedLabel.setAlignment(Pos.CENTER_RIGHT);
		lastRefreshedLabel.getStyleClass().add("refresh-label");
		
		HBox refreshBox = new HBox(10);
		refreshBox.setAlignment(Pos.CENTER_LEFT);
		refreshBox.getChildren().addAll(refreshButton, lastRefreshedLabel);
		refreshBox.getStyleClass().add("refresh-box");

		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e -> submitIdea());
		submitButton.getStyleClass().add("submit-button");

		ideaTable = new TableView<>();
		//stretch the table
		VBox.setVgrow(ideaTable, Priority.ALWAYS);
		ideaTable.getStyleClass().add("idea-table");
		
		ideaTable.setRowFactory(tv -> new TableRow<Idea>() {
		    @Override
		    protected void updateItem(Idea item, boolean empty) {
		        super.updateItem(item, empty);
		        
		        // Clear old style classes each update
		        getStyleClass().removeAll("row-pending", "row-processed");
		        if (item != null && !empty) {
		            switch (item.getStatus()) {
		                case "Pending":
		                    getStyleClass().add("row-pending");
		                    break;
		                case "Processed":
		                    getStyleClass().add("row-processed");
		                    break;
		            }
		        }
		    }
		});
		setupTable();
		
		
		//Left side form
		ideaLayout.getChildren().addAll(
				ideaLabel,
				ideaArea,
				ideaBenefitLabel,
				ideaBenefitArea,
				commentSuggestionLabel,
				commentArea,
				submitButton);
		
		
		//Add table and associated controls to the right(view) layout
		viewLayout.getChildren().addAll(refreshBox, new Label("Your Ideas"), ideaTable);
		
		
		//combine into main layout
		mainLayout.getChildren().addAll(ideaLayout, viewLayout);
		
		loadIdeas();//Load ideas initailly from the database
		startAutoRefresh(); //refresh loaded data periodically to reflect all ideas added including the recentones from the database
	
	}

	private void setupTable() {
		TableColumn<Idea, Integer> userIdCol = new TableColumn<>("User ID");
		//asObject() wraps the primitive int property returned by getUserID() to make it an Integer object.
		userIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUserID()).asObject());
		TableColumn<Idea, String> ideaCol = new TableColumn<>("Idea");
		ideaCol
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdeaText()));

		TableColumn<Idea, String> benefitsCol = new TableColumn<>("Benefits");
		benefitsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBenefits()));
		
		TableColumn<Idea, String> commentCol = new TableColumn<>("Comments");
		commentCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComments()));

		TableColumn<Idea, String> dateCol = new TableColumn<>("Date Submitted");
		dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateSubmitted()));

		TableColumn<Idea, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

		ideaTable.getColumns().addAll(userIdCol,ideaCol, benefitsCol, commentCol, dateCol, statusCol);
	}

	private void submitIdea() {
		
		String ideaText = ideaArea.getText().trim();
		String benefits = ideaBenefitArea.getText().trim();
		String comments = commentArea.getText().trim();
		
		if(ideaText.isEmpty() || benefits.isEmpty() || comments.isEmpty()) {
			showAlert("Submission Error", "Please fill all fields to submit your idea!");
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
					"INSERT INTO ideas (user_id, idea_text, idea_benefits, idea_comments, date_submitted, status) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, userId);
			stmt.setString(2, ideaText);
			stmt.setString(3, benefits);
			stmt.setString(4, comments);
			stmt.setString(5, formattedCurrentDateTime);
			stmt.setString(6, "Pending");
			stmt.executeUpdate();
			clearFields(); //clear all fields to prevent submitting duplicate ideas.
			loadIdeas();
			ToastUtils.showToast(mainLayout.getScene(), "Your idea was submitted");
		} catch (Exception e) {
			e.printStackTrace();
			showAlert("Error","Failed to submit idea.");
		}
	}

	private void loadIdeas() {
		
		ObservableList<Idea> ideas = FXCollections.observableArrayList();
		try (Connection conn = Database.getConnection()) {
			String ideaQuery = "SELECT user_id, idea_text, idea_benefits, idea_comments, date_submitted, status FROM ideas WHERE user_id= ?";
			int userId = findUserId(username);
			PreparedStatement stmt = conn.prepareStatement(ideaQuery);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ideas.add(new Idea(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		}
		
		int newCount = ideas.size() - previousIdeaCount;
		ideaTable.setItems(ideas);
		
		lastRefreshedLabel.setText("Last Refreshed at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		
		//Detect if any new ideas are added
		if(newCount > 0) {
			
			ToastUtils.showToast(mainLayout.getScene(), newCount + " new idea(s) detected.");
			highlightNewRows(newCount);
		}
		previousIdeaCount = ideas.size();

	}

	public HBox getView() {
		return mainLayout;
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
	
	private void highlightNewRows(int count) {
		//wait until table view has its new rows displayed.
		PauseTransition delay = new PauseTransition(Duration.millis(200));
		delay.setOnFinished(e -> {
			int totalRows = ideaTable.getItems().size();
			for (int i = totalRows - count; i < totalRows; i++) {
				TableRowHighlighter.highlightRow(ideaTable, i);
			}
		});
		delay.play();
	}

}
