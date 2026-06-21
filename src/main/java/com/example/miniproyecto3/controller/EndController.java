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

public class EndController {



    @FXML private Button menuBtn;
    @FXML private Button startBtn;

    // Labels definidos en EndView.fxml (los que muestran el ganador y stats)
    // Se buscan por fx:id; si tu FXML no les pone fx:id todavía,
    // los inyectamos por lookup en initEndGame().
    @FXML private Label winnerNameLabel;   // fx:id="winnerNameLabel"
    @FXML private Label finalScoreLabel;   // fx:id="finalScoreLabel"

    @FXML private Label resultLabel;

    @FXML private Label movesLabel;

    private int machineCount;

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
     * Recibe los datos del ganador desde GameController y los muestra en pantalla.
     *
     * @param winner       Jugador ganador
     * @param machineCount Número de IAs con las que se jugó (para reiniciar igual)
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


    /** Cierra esta pantalla y vuelve al menú principal. */
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

    /** Cierra esta pantalla e inicia una nueva partida con el mismo número de IAs. */
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