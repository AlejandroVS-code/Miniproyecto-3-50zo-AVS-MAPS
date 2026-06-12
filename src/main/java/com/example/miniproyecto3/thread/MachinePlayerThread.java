package com.example.miniproyecto3.thread;

import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.GameConstants;
import com.example.miniproyecto3.model.GameModel;
import com.example.miniproyecto3.model.MachinePlayer;
import javafx.application.Platform;

import java.util.Random;

public class MachinePlayerThread extends Thread {

    private final GameModel gameModel;
    private final Runnable onTurnComplete;
    private final Random random;

    public MachinePlayerThread(GameModel gameModel, Runnable onTurnComplete) {
        this.gameModel = gameModel;
        this.onTurnComplete = onTurnComplete;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            int delay = GameConstants.MACHINE_PLAY_MIN_DELAY +
                    random.nextInt(GameConstants.MACHINE_PLAY_MAX_DELAY -
                            GameConstants.MACHINE_PLAY_MIN_DELAY);
            Thread.sleep(delay);

            MachinePlayer machine = (MachinePlayer) gameModel.getCurrentPlayer();
            Card cardToPlay = machine.selectBestCard(gameModel.getTablePile().getCurrentSum());

            if (cardToPlay != null) {
                machine.playCard(cardToPlay, gameModel.getTablePile());
            } else {
                gameModel.eliminateCurrentPlayer();
            }

            Platform.runLater(onTurnComplete);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Platform.runLater(onTurnComplete);
        }
    }
}