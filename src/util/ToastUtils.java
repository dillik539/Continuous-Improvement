package util;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.Duration;
/**
 * 
 * @author Dilli Khatiwoda
 * 
 *Implementation of toast-style(non-blocking) popup for information.
 *Toast-style popup is a small, but non-intrusive notification that appears temporarily,
 *does not block the interaction, and disappears after a few seconds. 
 *It is ideal for quick messages to the user.
 */

public class ToastUtils {
	
	public static void showToast(Scene scene, String message) {
		if (scene == null) return;

	    Platform.runLater(() -> {
	        Label toastLabel = new Label(message);
	        toastLabel.setStyle(
	            "-fx-background-color: linear-gradient(to right, #0078d7, #3399ff);" + // Blue gradient
	            "-fx-text-fill: white;" +
	            "-fx-font-weight: bold;" +
	            "-fx-padding: 10 20;" +
	            "-fx-background-radius: 8;" +
	            "-fx-border-radius: 8;" +
	            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);"
	        );
	        toastLabel.setOpacity(0);

	        StackPane toastPane = new StackPane(toastLabel);
	        toastPane.setPrefWidth(scene.getWidth());
	        toastPane.setAlignment(Pos.BOTTOM_CENTER);
	        toastPane.setTranslateY(-40);

	        Popup popup = new Popup();
	        popup.getContent().add(toastPane);
	        popup.setAutoFix(true);
	        popup.setAutoHide(true);
	        popup.show(scene.getWindow());

	        // Fade in/out animation
	        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.millis(400), toastLabel);
	        fadeIn.setFromValue(0);
	        fadeIn.setToValue(0.95);

	        PauseTransition delay = new PauseTransition(Duration.seconds(3));

	        javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(Duration.millis(400), toastLabel);
	        fadeOut.setFromValue(0.95);
	        fadeOut.setToValue(0);

	        fadeIn.setOnFinished(e -> delay.play());
	        delay.setOnFinished(e -> fadeOut.play());
	        fadeOut.setOnFinished(e -> popup.hide());

	        fadeIn.play();
	    });
    }
}