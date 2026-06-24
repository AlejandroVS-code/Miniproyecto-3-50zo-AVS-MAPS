package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX {@link Stage} that loads and displays the main game screen
 * (GameView.fxml). Creating an instance immediately loads the FXML,
 * configures the window (title, icon, fixed size), shows it, and exposes
 * the associated {@link GameController} so callers can initialize the match
 * (see {@code GameController#initGame}).
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class GameStage extends Stage {

    private GameController controller;

    /**
     * Loads GameView.fxml, configures this window, shows it, and
     * captures the controller instance created by the {@link FXMLLoader}.
     *
     * @throws IOException if the FXML file fails to load.
     */
    public GameStage() throws IOException {

        Image icon = new Image(getClass().getResourceAsStream("/com/example/Miniproyecto3/Imagenes/main.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/Miniproyecto3/Vistas/GameView.fxml")
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

    /**
     * @return the controller bound to this stage's FXML, used to initialize
     *         and drive the match.
     */
    public GameController getController() {
        return controller;
    }
}