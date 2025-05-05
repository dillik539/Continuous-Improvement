package util;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TableRowHighlighter {

	public static void highlightRow(TableView<?> tableView, int rowIndex) {
		if (rowIndex < 0 || rowIndex >= tableView.getItems().size()) return;

		// Force layout pass to ensure row is visible
		tableView.layout();

		// Try to get the row directly
		TableRow<?> row = getRow(tableView, rowIndex);
		if (row != null) {
			String originalStyle = row.getStyle();
			row.setStyle("-fx-background-color: lightgreen;");

			FadeTransition fade = new FadeTransition(Duration.seconds(3), row);
			fade.setFromValue(1.0);
			fade.setToValue(1.0); // just delay
			fade.setOnFinished(e -> row.setStyle(originalStyle));
			fade.play();
		}
	}

	private static TableRow<?> getRow(TableView<?> tableView, int index) {
		for (Node node : tableView.lookupAll(".table-row-cell")) {
			if (node instanceof TableRow<?> row && row.getIndex() == index) {
				return row;
			}
		}
		return null;
	}
}
