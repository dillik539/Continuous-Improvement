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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Database;
import model.Idea;
import util.TableRowHighlighter;
import util.ToastUtils;

public class AdminController {
	private TableView<Idea> ideaTable;
	private VBox layout;
	private Label lastRefreshedLabel;
	private Button refreshButton;
	private int previousIdeaCount = 0;

	public AdminController() {
		layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 40, 20)); //more bottom space
		
		//Admin Panel Label
		Label adminPanelLabel = new Label("Admin Panel");
		adminPanelLabel.getStyleClass().add("panel-title");
		adminPanelLabel.setMaxWidth(Double.MAX_VALUE);
		adminPanelLabel.setAlignment(Pos.CENTER);
		
		//---- Buttons -----
		Button processButton = new Button("Process Idea");
		processButton.getStyleClass().add("action-button");
		processButton.setOnAction(e -> processIdea());
		
		Button addUserButton = new Button("Add New User");
		addUserButton.getStyleClass().add("action-button");
		addUserButton.setOnAction(e -> new AddUserController().show());
		
		refreshButton = new Button("Refresh Now");
		refreshButton.getStyleClass().add("action-button");
		refreshButton.setOnAction(e -> loadIdeas());
		
		lastRefreshedLabel = new Label("Last refreshed at: --");
		lastRefreshedLabel.getStyleClass().add("refresh-label");
		
		// --- Buttons and labels align horizontally ----
		Region spacer = new Region(); //This pushes lastRefreshedLabel to the right corner
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox topControls = new HBox(15, processButton, addUserButton, refreshButton, spacer, lastRefreshedLabel);
		topControls.setAlignment(Pos.CENTER_LEFT);

		//lastRefreshedLabel.setAlignment(Pos.CENTER_RIGHT);
		
		//----- Admin Header ----
		VBox adminHeader = new VBox(10, adminPanelLabel, topControls);
		adminHeader.getStyleClass().add("panel-header");

		ideaTable = new TableView<>();
		ideaTable.setRowFactory(tv -> new TableRow<Idea>() {
			@Override
			protected void updateItem(Idea item, boolean empty) {
				super.updateItem(item, empty);
				getStyleClass().removeAll("row-pending", "row-processed");
				if(item != null && !empty) {
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
		
		VBox.setVgrow(ideaTable, Priority.ALWAYS);
		
		layout.getChildren().addAll(adminHeader, ideaTable);

		loadIdeas();//load ideas from the database initially
		startAutoRefresh(); //auto refresh periodically.
	}

	private void setupTable() {
		TableColumn<Idea, Integer> userIdCol = new TableColumn<>("Submitted By");
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

	private void loadIdeas() {
		ObservableList<Idea> ideas = FXCollections.observableArrayList();
		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("SELECT user_id, short_description, idea_text, date_submitted, status FROM ideas");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ideas.add(new Idea(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int newCount = ideas.size() - previousIdeaCount;
		ideaTable.setItems(ideas);
		lastRefreshedLabel.setText("Last refreshed at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		
		if(newCount > 0) {
			ToastUtils.showToast(layout.getScene(), newCount + " new idea(s) detected.");
			highlightNewRows(newCount);

		}
		previousIdeaCount = ideas.size();
	}

	private void processIdea() {
		Idea selectedIdea = ideaTable.getSelectionModel().getSelectedItem();
		if (selectedIdea == null) {
			return;
		}

		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn
					.prepareStatement("UPDATE ideas SET status = 'Processed' WHERE user_id = ? AND short_description = ?");
			stmt.setInt(1, selectedIdea.getUserID());
			stmt.setString(2, selectedIdea.getShortDescription());
			stmt.executeUpdate();
			loadIdeas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VBox getView() {
		return layout;
	}
	private void startAutoRefresh() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> loadIdeas()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
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