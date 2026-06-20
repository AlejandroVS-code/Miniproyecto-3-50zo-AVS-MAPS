package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.view.GameStage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import com.example.miniproyecto3.util.MusicManager;
import com.example.miniproyecto3.util.AnimationUtil;

import java.io.IOException;

public class HomeController {

    @FXML private Button selection1Ia;
    @FXML private Button selection2Ia;
    @FXML private Button selection3Ia;
    @FXML private Button startBtn;
    @FXML private Button exitBtn;

    private int selectedMachineCount = 0;

    @FXML
    public void initialize() {

        MusicManager.playMusic("/com/example/miniproyecto3/Audio/menu.wav");
        selection1Ia.setOnAction(e -> handleSelection(1));
        selection2Ia.setOnAction(e -> handleSelection(2));
        selection3Ia.setOnAction(e -> handleSelection(3));
        startBtn.setOnAction(e -> handleStart());
        exitBtn.setOnAction(e -> handleExit());

        AnimationUtil.addHoverEffect(startBtn);
        AnimationUtil.addHoverEffect(exitBtn);

        AnimationUtil.addHoverEffect(selection1Ia);
        AnimationUtil.addHoverEffect(selection2Ia);
        AnimationUtil.addHoverEffect(selection3Ia);

        AnimationUtil.addPressEffect(startBtn);
        AnimationUtil.addPressEffect(exitBtn);

        AnimationUtil.addPressEffect(selection1Ia);
        AnimationUtil.addPressEffect(selection2Ia);
        AnimationUtil.addPressEffect(selection3Ia);

    }

    private void handleSelection(int count) {
        selectedMachineCount = count;
        selection1Ia.getStyleClass().removeAll("selection-btn-active");
        selection2Ia.getStyleClass().removeAll("selection-btn-active");
        selection3Ia.getStyleClass().removeAll("selection-btn-active");

        switch (count) {
            case 1 -> selection1Ia.getStyleClass().add("selection-btn-active");
            case 2 -> selection2Ia.getStyleClass().add("selection-btn-active");
            case 3 -> selection3Ia.getStyleClass().add("selection-btn-active");
        }
    }

    private void handleStart() {
        if (selectedMachineCount == 0) {
            return;
        }
        try {
            MusicManager.stopMusic(); // detener menú antes de abrir juego
            Stage stage = (Stage) startBtn.getScene().getWindow();
            stage.close();

            GameStage gameStage = new GameStage();
            gameStage.getController().initGame(new GameModel(), selectedMachineCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExit() {
        MusicManager.stopMusic();
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }


}