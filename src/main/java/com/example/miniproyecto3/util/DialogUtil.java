package com.example.miniproyecto3.util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogUtil {

    private DialogUtil() {}

    public static void showError(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        Label msg = new Label(message);
        msg.setStyle("-fx-text-fill: #E5E5E5; -fx-font-size: 14; -fx-font-weight: bold;");

        Button btn = new Button("Entendido");
        btn.setStyle(
                "-fx-background-color: #c9a14a; -fx-text-fill: #1f1f1f; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;"
        );
        btn.setOnAction(e -> dialog.close());

        VBox layout = new VBox(16, msg, btn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: #0B1220; -fx-border-color: #c9a14a; " +
                        "-fx-border-width: 2; -fx-border-radius: 10; " +
                        "-fx-background-radius: 10; -fx-padding: 24;"
        );

        Scene scene = new Scene(layout);
        scene.setFill(null);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public static int showAceDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);

        final int[] result = {1};

        Label title = new Label("¿Cuánto vale el As?");
        title.setStyle("-fx-text-fill: #D4AF37; -fx-font-size: 16; -fx-font-weight: bold;");

        Button btn1 = new Button("Sumar 1");
        btn1.setStyle(
                "-fx-background-color: #4d3a12; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8; " +
                        "-fx-border-color: #c9a14a; -fx-border-radius: 8; " +
                        "-fx-cursor: hand; -fx-pref-width: 100;"
        );
        btn1.setOnAction(e -> {
            result[0] = 1;
            dialog.close();
        });

        Button btn10 = new Button("Sumar 10");
        btn10.setStyle(
                "-fx-background-color: #c9a14a; -fx-text-fill: #1f1f1f; " +
                        "-fx-font-weight: bold; -fx-background-radius: 8; " +
                        "-fx-cursor: hand; -fx-pref-width: 100;"
        );
        btn10.setOnAction(e -> {
            result[0] = 10;
            dialog.close();
        });

        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(12, btn1, btn10);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, title, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
                "-fx-background-color: #0B1220; -fx-border-color: #c9a14a; " +
                        "-fx-border-width: 2; -fx-border-radius: 10; " +
                        "-fx-background-radius: 10; -fx-padding: 28;"
        );

        Scene scene = new Scene(layout);
        scene.setFill(null);
        dialog.setScene(scene);
        dialog.showAndWait();

        return result[0];
    }
}