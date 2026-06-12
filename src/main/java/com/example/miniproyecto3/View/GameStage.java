package com.example.miniproyecto3.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GameStage extends Stage {

    public GameStage() throws IOException {

        Image icon = new Image(getClass().getResourceAsStream("/com/example/Miniproyecto3/Imagenes/main.png"));


        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/Miniproyecto3/Vistas/GameView.fxml")
        );

        Parent root = loader.load();
        this.setResizable(false);
        this.setTitle("Cincuentazo");
        this.getIcons().add(icon);

        Scene scene = new Scene(root);
        setScene(scene);
        show();
    }

}
