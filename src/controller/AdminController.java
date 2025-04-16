package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Database;
import model.Idea;

public class AdminController {
	private Stage adminStage;
	private TableView<Idea> ideaTable;

	public AdminController() {
		adminStage = new Stage();
		adminStage.setTitle("Admin Panel");

		ideaTable = new TableView<>();
		setupTable();

		Button processButton = new Button("Process Idea");
		processButton.setOnAction(e -> processIdea());

		VBox layout = new VBox(10, ideaTable, processButton);
		adminStage.setScene(new Scene(layout, 500, 400));

		loadIdeas();
	}

	private void setupTable() {
		TableColumn<Idea, String> shortDescCol = new TableColumn<>("Short Description");
		shortDescCol
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShortDescription()));

		TableColumn<Idea, String> descCol = new TableColumn<>("Idea");
		descCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullIdea()));

		TableColumn<Idea, String> dateCol = new TableColumn<>("Date Submitted");
		dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateSubmitted()));

		TableColumn<Idea, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

		ideaTable.getColumns().addAll(shortDescCol, descCol, dateCol, statusCol);
	}

	private void loadIdeas() {
		ObservableList<Idea> ideas = FXCollections.observableArrayList();
		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ideas");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ideas.add(new Idea(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ideaTable.setItems(ideas);
	}

	private void processIdea() {
		Idea selectedIdea = ideaTable.getSelectionModel().getSelectedItem();
		if (selectedIdea == null) {
			return;
		}

		try (Connection conn = Database.getConnection()) {
			PreparedStatement stmt = conn
					.prepareStatement("UPDATE ideas SET status = 'Processed' WHERE description = ?");
			stmt.setString(1, selectedIdea.getShortDescription());
			stmt.executeUpdate();
			loadIdeas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void show() {
		adminStage.show();
	}
}