package com.example.miniproyecto3.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimationUtil {

    private AnimationUtil() {}

    public static void fadeInCard(ImageView card) {
        card.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
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
}
