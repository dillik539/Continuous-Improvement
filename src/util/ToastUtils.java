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
            toastLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 5;");
            toastLabel.setOpacity(0.9);

            StackPane toastPane = new StackPane(toastLabel);
            toastPane.setPrefWidth(scene.getWidth());
            toastPane.setAlignment(Pos.BOTTOM_CENTER);
            toastPane.setTranslateY(-40);

            // Use a Popup instead of adding to root
            Popup popup = new Popup();
            popup.getContent().add(toastPane);
            popup.setAutoFix(true);
            popup.setAutoHide(true);
            popup.show(scene.getWindow());

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> popup.hide());
            delay.play();
        });
    }
}