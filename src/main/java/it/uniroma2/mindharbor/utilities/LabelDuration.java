package it.uniroma2.mindharbor.utilities;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;
/**
 * LabelDuration provides utility methods to apply fade transitions to labels in a JavaFX application.
 * This class is designed to enhance the user interface by allowing messages on labels to fade in and fade out.
 */
public class LabelDuration {
    /**
     * Applies a fade-in and fade-out effect to a label with a specified message.
     * The message appears with a fade-in effect, remains visible for the duration of the transition, and then fades out.
     *
     * @param label    The label to which the fade transitions will be applied.
     * @param message  The message to display on the label during the transition.
     */
    public void duration(Label label, String message) {
        Duration duration = Duration.seconds(5); // Defines the duration of both the fade-in and fade-out effects.
        FadeTransition fadeIn = new FadeTransition(duration, label);
        fadeIn.setFromValue(0.0); // Start fully transparent.
        fadeIn.setToValue(1.0);   // End fully opaque.

        FadeTransition fadeOut = new FadeTransition(duration, label);
        fadeOut.setFromValue(1.0); // Start fully opaque.
        fadeOut.setToValue(0.0);   // End fully transparent.

        label.setText(message); // Set the text of the label to the provided message.

        fadeIn.play(); // Start the fade-in effect.
        fadeIn.setOnFinished(event -> fadeOut.play()); // Once fade-in completing, start fade-out.
    }
}
