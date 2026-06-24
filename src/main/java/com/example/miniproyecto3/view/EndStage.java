package com.example.miniproyecto3.view;

import com.example.miniproyecto3.controller.EndController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX {@link Stage} that loads and displays the results screen
 * (EndView.fxml). Creating an instance immediately loads the FXML,
 * configures the window (title, icon, fixed size), shows it, and exposes
 * the associated {@link EndController} so callers can populate it with the
 * match's outcome (see {@code EndController#initEndGame}).
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class EndStage extends Stage {

    private EndController controller;

    /**
     * Loads EndView.fxml, configures this window, shows it, and
     * captures the controller instance created by the {@link FXMLLoader}.
     *
     * @throws IOException if the FXML file fails to load.
     */
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

    /**
     * @return the controller bound to this stage's FXML, used to populate
     *         the results screen with the match outcome.
     */
    public EndController getController() {
        return controller;
    }
}