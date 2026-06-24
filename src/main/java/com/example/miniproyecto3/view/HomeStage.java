package com.example.miniproyecto3.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX {@link Stage} that loads and displays the home/main menu screen
 * (HomeView.fxml). Creating an instance immediately loads the FXML,
 * configures the window (title, icon, fixed size), and shows it.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class HomeStage extends Stage {

    /**
     * Loads HomeView.fxml, configures this window, and shows it.
     *
     * @throws IOException if the FXML file fails to load.
     */
    public HomeStage() throws IOException {

        Image icon = new Image(getClass().getResourceAsStream("/com/example/Miniproyecto3/Imagenes/main.png"));

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/Miniproyecto3/Vistas/HomeView.fxml")
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