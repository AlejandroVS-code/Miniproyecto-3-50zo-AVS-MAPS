package com.example.miniproyecto3.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;

import java.util.List;

/**
 * Collection of static helper methods that apply JavaFX animations (and, in
 * a few cases, paired sound effects) to UI nodes: card transitions, button
 * hover/press feedback, turn highlighting, and elimination/error feedback.
 *
 * This class cannot be instantiated; all functionality is exposed
 * through static methods.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */

public class AnimationUtil {

    private AnimationUtil() {}

    /**
     * Fades a card image view in from fully transparent to fully opaque.
     * Used when revealing a newly drawn or newly placed card.
     *
     * @param card the card view to fade in.
     */
    public static void fadeInCard(ImageView card) {
        card.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Applies a red glow and a quick horizontal shake to a card view to
     * signal an invalid play attempt.
     *
     * @param card the card view to shake.
     */
    public static void invalidCardShake(ImageView card) {
        card.setStyle("-fx-effect: dropshadow(gaussian, #ff0000, 12, 0.8, 0, 0);");

        TranslateTransition shake = new TranslateTransition(Duration.millis(60), card);
        shake.setFromX(0);
        shake.setByX(8);
        shake.setCycleCount(5);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> {
            card.setTranslateX(0);
            card.setStyle("");
        });
        shake.play();
    }

    /**
     * Fades a card view out, typically used when an AI player plays a card
     * onto the table, then resets its opacity and runs the given callback.
     *
     * @param card     the card view to animate.
     * @param onFinish callback invoked once the animation completes (may be {@code null}).
     */
    public static void playCardToTable(ImageView card, Runnable onFinish) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), card);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            card.setOpacity(1);
            if (onFinish != null) onFinish.run();
        });
        fade.play();
    }

    /**
     * Scales a card view up briefly to indicate it has been selected by the
     * player.
     *
     * @param card the card view to pulse.
     */
    public static void selectCardPulse(ImageView card) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), card);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.15);
        scale.setToY(1.15);
        scale.setCycleCount(1);
        scale.play();
    }

    /**
     * Scales a card view back down to its normal size, undoing
     * {@link #selectCardPulse(ImageView)}.
     *
     * @param card the card view to reset.
     */
    public static void deselectCard(ImageView card) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), card);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    /**
     * Shakes a player's icon horizontally to signal that the player has
     * just been eliminated.
     *
     * @param icon the player icon view to shake.
     */
    public static void eliminatedShake(ImageView icon) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(80), icon);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> icon.setTranslateX(0));
        shake.play();
    }

    /**
     * Pulses a player's icon to draw attention to whose turn is currently active.
     *
     * @param icon the player icon view to pulse.
     */
    public static void highlightTurn(ImageView icon) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(300), icon);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(2);
        pulse.setAutoReverse(true);
        pulse.play();
    }

    /**
     * Animates a group of card views moving toward a target deck view
     * (translating and fading out simultaneously), compensating for any
     * rotation applied to each card's parent container, then hides them and
     * resets their transform/opacity once the animation finishes.
     *
     * Used both when a card is played (moving toward the discard pile)
     * and when a player is eliminated (their hand moving back into the draw deck).
     *
     * @param cards    the card views to animate; invisible cards are skipped.
     * @param deck     the target deck/pile view to animate the cards toward.
     * @param onFinish callback invoked once every card has finished animating (may be {@code null}).
     */
    public static void sendCardsToDeck(List<ImageView> cards, ImageView deck, Runnable onFinish) {
        if (cards.isEmpty()) {
            if (onFinish != null) onFinish.run();
            return;
        }

        javafx.animation.ParallelTransition allCards = new javafx.animation.ParallelTransition();

        javafx.geometry.Bounds deckBounds = deck.localToScreen(deck.getBoundsInLocal());

        for (ImageView card : cards) {
            if (!card.isVisible()) continue;

            javafx.geometry.Bounds cardBounds = card.localToScreen(card.getBoundsInLocal());

            double deltaX = deckBounds.getCenterX() - cardBounds.getCenterX();
            double deltaY = deckBounds.getCenterY() - cardBounds.getCenterY();

            // Compensate for the parent container's rotation
            double rotation = card.getParent() != null
                    ? card.getParent().getRotate()
                    : 0;

            double rad = Math.toRadians(rotation);
            double adjustedX = deltaX * Math.cos(rad) + deltaY * Math.sin(rad);
            double adjustedY = -deltaX * Math.sin(rad) + deltaY * Math.cos(rad);

            TranslateTransition move = new TranslateTransition(Duration.millis(500), card);
            move.setByX(adjustedX);
            move.setByY(adjustedY);

            FadeTransition fade = new FadeTransition(Duration.millis(500), card);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            javafx.animation.ParallelTransition cardAnim =
                    new javafx.animation.ParallelTransition(move, fade);

            allCards.getChildren().add(cardAnim);
        }

        allCards.setOnFinished(e -> {
            for (ImageView card : cards) {
                card.setTranslateX(0);
                card.setTranslateY(0);
                card.setOpacity(1);
                card.setVisible(false);
            }
            if (onFinish != null) onFinish.run();
        });

        allCards.play();
    }

    /**
     * Attaches a hover visual effect (slight scale-up plus a glow) and the
     * hover sound effect to a button.
     *
     * @param button the button to enhance.
     */
    public static void addHoverEffect(Button button) {

        Glow glow = new Glow(0.35);

        button.setOnMouseEntered(e -> {

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.08);
            scale.setToY(1.08);
            scale.play();

            button.setEffect(glow);

            MusicManager.playSoundEffect("/com/example/miniproyecto3/Audio/hover.wav");
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            button.setEffect(null);
        });

    }

    /**
     * Attaches a press visual effect (slight scale-down while held) and the
     * click sound effect to a button.
     *
     * @param button the button to enhance.
     */
    public static void addPressEffect(Button button) {

        button.setOnMousePressed(e -> {
            button.setScaleX(0.95);
            button.setScaleY(0.95);

            MusicManager.playSoundEffect("/com/example/miniproyecto3/Audio/click.wav");
        });

        button.setOnMouseReleased(e -> {
            button.setScaleX(1);
            button.setScaleY(1);
        });

    }
}
