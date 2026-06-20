package com.example.miniproyecto3.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;

import java.util.List;

public class AnimationUtil {

    private AnimationUtil() {}

    public static void fadeInCard(ImageView card) {
        card.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
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

    public static void selectCardPulse(ImageView card) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), card);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.15);
        scale.setToY(1.15);
        scale.setCycleCount(1);
        scale.play();
    }

    public static void deselectCard(ImageView card) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), card);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.play();
    }

    public static void eliminatedShake(ImageView icon) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(80), icon);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> icon.setTranslateX(0));
        shake.play();
    }

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

            // Compensar la rotacion del contenedor padre
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
    public static void addHoverEffect(Button button) {

        Glow glow = new Glow(0.35);

        button.setOnMouseEntered(e -> {

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.08);
            scale.setToY(1.08);
            scale.play();

            button.setEffect(glow);
        });

        button.setOnMouseExited(e -> {

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            button.setEffect(null);
        });

    }

    public static void addPressEffect(Button button) {

        button.setOnMousePressed(e -> {
            button.setScaleX(0.95);
            button.setScaleY(0.95);
        });

        button.setOnMouseReleased(e -> {
            button.setScaleX(1);
            button.setScaleY(1);
        });

    }
}
