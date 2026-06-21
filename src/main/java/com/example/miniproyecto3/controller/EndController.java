package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.model.Player;
import com.example.miniproyecto3.model.enums.PlayerType;
import com.example.miniproyecto3.view.EndStage;
import com.example.miniproyecto3.view.GameStage;
import com.example.miniproyecto3.view.HomeStage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.miniproyecto3.util.MusicManager;
import com.example.miniproyecto3.util.AnimationUtil;


import java.io.IOException;

/**
 * Controller for the results screen (EndView.fxml), shown once a
 * match ends.
 *
 * Displays the winner's name, final score, and total moves, and shows
 * "VICTORIA" in gold if the human player won, or "¡FALLASTE!" in red along
 * with the winning AI's name and score otherwise. Also lets the player
 * return to the main menu or start a new match with the same number of AI
 * opponents.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
public class EndController {



    @FXML private Button menuBtn;
    @FXML private Button startBtn;

    // Labels defined in EndView.fxml (the ones showing the winner and stats).
    // They are looked up by fx:id; if your FXML does not set fx:id yet,
    // inject them via lookup in initEndGame().
    @FXML private Label winnerNameLabel;   // fx:id="winnerNameLabel"
    @FXML private Label finalScoreLabel;   // fx:id="finalScoreLabel"

    @FXML private Label resultLabel;

    @FXML private Label movesLabel;

    private int machineCount;

    /**
     * Called automatically by JavaFX after the FXML fields are injected.
     * Wires the Menu and New Game buttons' actions, and attaches hover/press
     * animations and sound effects to both.
     */
    @FXML
    public void initialize() {


        menuBtn.setOnAction(e -> handleMenu());
        startBtn.setOnAction(e -> handleNewGame());

        AnimationUtil.addHoverEffect(menuBtn);
        AnimationUtil.addPressEffect(menuBtn);

        AnimationUtil.addHoverEffect(startBtn);
        AnimationUtil.addPressEffect(startBtn);
    }

    /**
     * Receives the winner's data from GameController and displays it
     * on screen, including whether the human player won or lost.
     *
     * @param winner       the winning player.
     * @param machineCount number of AI opponents the match was played with
     *                     (used to restart with the same configuration).
     * @param totalMoves   total moves made by the winner during the match.
     */
    public void initEndGame(
            Player winner,
            int machineCount,
            int totalMoves) {

        this.machineCount = machineCount;
        MusicManager.playMusic("/com/example/miniproyecto3/Audio/end.wav");

        boolean humanWon = winner.getPlayerType() == PlayerType.HUMAN;

        if (humanWon) {
            resultLabel.setText("VICTORIA");
            resultLabel.setStyle("-fx-text-fill: #d6a43c; -fx-font-size: 14; -fx-font-weight: bold;");
        } else {
            resultLabel.setText("¡FALLASTE!");
            resultLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14; -fx-font-weight: bold;");
        }

        winnerNameLabel.setText(
                winner.getName()
        );

        finalScoreLabel.setText(
                String.valueOf(winner.getTotalPoints())
        );

        movesLabel.setText(
                String.valueOf(totalMoves)
        );
    }


    /** Closes this screen and returns to the main menu. */
    private void handleMenu() {
        try {
            MusicManager.stopMusic();
            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.close();
            new HomeStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Closes this screen and starts a new match with the same number of AI opponents. */
    private void handleNewGame() {
        try {
            MusicManager.stopMusic();
            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.close();

            GameStage gameStage = new GameStage();
            gameStage.getController().initGame(new GameModel(), machineCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}