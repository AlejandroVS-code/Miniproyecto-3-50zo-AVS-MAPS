package com.example.miniproyecto3;

import com.example.miniproyecto3.view.HomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the Cincuentazo card game application.
 *
 * This class is responsible for launching the JavaFX application
 * and initializing the main window of the game by opening the
 * home screen ({@link HomeStage}), from which the player can
 * configure and start a match.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */

public class Main extends Application {

    /**
     * Called by the JavaFX runtime after launch. Opens the home screen.
     *
     * @param primaryStage the primary stage provided by JavaFX (unused;
     *                     this application manages its own {@link Stage}
     *                     instances per screen instead).
     * @throws IOException if the home screen's FXML fails to load.
     */
    public void start(Stage primaryStage) throws IOException {

        new HomeStage();
    }
}
