package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.EndController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class EndStage extends Stage {

    private EndController controller;

    public EndStage() throws IOException {

        Image icon = new Image(getClass().getResourceAsStream("/com/example/miniproyecto3/Imagenes/main.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/miniproyecto3/Vistas/EndView.fxml")
        );

        Parent root = loader.load();
        this.controller = loader.getController();
        this.setResizable(false);
        this.setTitle("Cincuentazo");
        this.getIcons().add(icon);

        Scene scene = new Scene(root);
        setScene(scene);
        show();
    }

    public EndController getController() {
        return controller;
    }
}