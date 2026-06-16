package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.*;
import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.GameStateException;
import com.example.miniproyecto3.model.interfaces.ICardClickHandler;
import com.example.miniproyecto3.thread.DrawCardThread;
import com.example.miniproyecto3.thread.MachinePlayerThread;
import com.example.miniproyecto3.util.AnimationUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import com.example.miniproyecto3.util.MusicManager;


import com.example.miniproyecto3.view.EndStage;

import java.io.IOException;
import java.util.List;

public class GameController {
    private List<ImageView> playerIcons;
    @FXML private Label pointsPj1;
    @FXML private Label pointsPj2;
    @FXML private Label pointsPj3;
    @FXML private Label pointsPj4;

    @FXML private Button moreBtn;
    private boolean machineAnimationInProgress = false;
    @FXML private ImageView drawDeck;
    @FXML private ImageView discardPile;
    @FXML private Label counterLabel;
    @FXML private Label maxLabel;

    @FXML private ImageView humanCard1;
    @FXML private ImageView humanCard2;
    @FXML private ImageView humanCard3;
    @FXML private ImageView humanCard4;

    @FXML private ImageView ia1Card1;
    @FXML private ImageView ia1Card2;
    @FXML private ImageView ia1Card3;
    @FXML private ImageView ia1Card4;

    @FXML private ImageView ia2Card1;
    @FXML private ImageView ia2Card2;
    @FXML private ImageView ia2Card3;
    @FXML private ImageView ia2Card4;

    @FXML private ImageView ia3Card1;
    @FXML private ImageView ia3Card2;
    @FXML private ImageView ia3Card3;
    @FXML private ImageView ia3Card4;

    @FXML private ImageView humanIcon;
    @FXML private ImageView ia1Icon;
    @FXML private ImageView ia2Icon;
    @FXML private ImageView ia3Icon;

    private GameModel gameModel;
    private int machineCount;
    private Card selectedCard;
    private boolean humanTurn;

    private int totalMoves = 0;

    private List<ImageView> humanCardViews;
    private List<ImageView> ia1CardViews;
    private List<ImageView> ia2CardViews;
    private List<ImageView> ia3CardViews;

    // ICardClickHandler como clase interna anónima
    private final ICardClickHandler cardClickHandler = new ICardClickHandler() {
        @Override
        public void onCardClicked(Card card, int index) {
            if (!humanTurn) return;
            selectedCard = card;
            highlightSelectedCard(index);
        }
    };
    private void highlightCurrentPlayer() {
        for (int i = 0; i < playerIcons.size(); i++) {
            if (i < gameModel.getPlayers().size()) {
                Player player = gameModel.getPlayers().get(i);
                playerIcons.get(i).getStyleClass()
                        .removeAll("player-active", "player-eliminated");

                if (player.isEliminated()) {
                    playerIcons.get(i).getStyleClass().add("player-eliminated");
                } else if (gameModel.getCurrentPlayerIndex() == i) {
                    playerIcons.get(i).getStyleClass().add("player-active");
                    AnimationUtil.highlightTurn(playerIcons.get(i));
                }
            }
        }
    }
    @FXML
    public void initialize() {
        MusicManager.playMusic(
                "/com/example/miniproyecto3/Audio/game.wav"
        );
        humanCardViews = List.of(humanCard1, humanCard2, humanCard3, humanCard4);
        ia1CardViews   = List.of(ia1Card1, ia1Card2, ia1Card3, ia1Card4);
        ia2CardViews   = List.of(ia2Card1, ia2Card2, ia2Card3, ia2Card4);
        ia3CardViews   = List.of(ia3Card1, ia3Card2, ia3Card3, ia3Card4);
        playerIcons = List.of(humanIcon, ia1Icon, ia2Icon, ia3Icon);
        moreBtn.setOnAction(e -> handlePlayCard());
        humanCardViews.forEach(v -> v.getStyleClass().add("human-card"));
        moreBtn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
                moveCardSelection(event.getCode() == KeyCode.RIGHT ? 1 : -1);
                event.consume();
            }
        });

        moreBtn.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        selectedCard = null;
                        clearHighlights();
                    } else if (event.getCode() == KeyCode.ENTER) {
                        handlePlayCard();
                    } else if (event.getCode() == KeyCode.DIGIT1) {
                        selectCardByIndex(0);
                    } else if (event.getCode() == KeyCode.DIGIT2) {
                        selectCardByIndex(1);
                    } else if (event.getCode() == KeyCode.DIGIT3) {
                        selectCardByIndex(2);
                    } else if (event.getCode() == KeyCode.DIGIT4) {
                        selectCardByIndex(3);
                    } else if (event.getCode() == KeyCode.RIGHT) {
                        moveCardSelection(1);
                    } else if (event.getCode() == KeyCode.LEFT) {
                        moveCardSelection(-1);
                    }
                });
            }
        });
    }

    private void moveCardSelection(int direction) {
        if (!humanTurn) return;
        List<Card> hand = gameModel.getPlayers().get(0).getHand();
        if (hand.isEmpty()) return;

        int currentIndex = selectedCard != null ? hand.indexOf(selectedCard) : -1;
        int newIndex = currentIndex + direction;

        if (newIndex < 0) newIndex = hand.size() - 1;
        if (newIndex >= hand.size()) newIndex = 0;

        cardClickHandler.onCardClicked(hand.get(newIndex), newIndex);
    }

    public void initGame(GameModel gameModel, int machineCount) {
        this.gameModel = gameModel;
        this.machineCount = machineCount;
        try {
            gameModel.initGame(machineCount);
            setupCardClickEvents();
            setupMachineVisibility();
            updateView();
            humanTurn = true;
        } catch (EmptyDeckException e) {
            showError("Error initializing game: " + e.getMessage());
        }
    }

    private void setupCardClickEvents() {
        List<Card> hand = gameModel.getPlayers().get(0).getHand();
        for (int i = 0; i < humanCardViews.size(); i++) {
            final int index = i;
            humanCardViews.get(i).setOnMouseClicked(e -> {
                if (index < hand.size()) {
                    cardClickHandler.onCardClicked(hand.get(index), index);
                }
            });
        }
    }

    private void setupMachineVisibility() {
        ia1Icon.setVisible(machineCount >= 1);
        ia2Icon.setVisible(machineCount >= 2);
        ia3Icon.setVisible(machineCount >= 3);
        ia1CardViews.forEach(v -> v.setVisible(machineCount >= 1));
        ia2CardViews.forEach(v -> v.setVisible(machineCount >= 2));
        ia3CardViews.forEach(v -> v.setVisible(machineCount >= 3));
    }

    private void handlePlayCard() {
        if (!humanTurn || selectedCard == null) return;
        try {
            HumanPlayer human = (HumanPlayer) gameModel.getPlayers().get(0);
            int index = human.getHand().indexOf(selectedCard);

            if (selectedCard.getRank() == Rank.ACE) {
                askAceValue(human, index);
            } else {
                AnimationUtil.playCardToTable(humanCardViews.get(index), () -> {
                    try {
                        playHumanCard(human, selectedCard);
                    } catch (Exception e) {
                        showError("Invalid play: that card would exceed 50.");
                    }
                });
            }
        } catch (Exception e) {
            showError("Invalid play: that card would exceed 50.");
        }
    }

    private void askAceValue(HumanPlayer human, int index) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ace");
        alert.setHeaderText("¿Cuánto vale el As?");

        ButtonType btn1 = new ButtonType("Sumar 1");
        ButtonType btn10 = new ButtonType("Sumar 10");
        alert.getButtonTypes().setAll(btn1, btn10);

        alert.showAndWait().ifPresent(choice -> {
            int aceValue = (choice == btn10) ? 10 : 1;
            AnimationUtil.playCardToTable(humanCardViews.get(index), () -> {
                try {
                    human.playCardWithValue(selectedCard, gameModel.getTablePile(), aceValue);
                    totalMoves++;
                    selectedCard = null;
                    clearHighlights();
                    humanTurn = false;
                    updateView();
                    startDrawCardThread(human);
                } catch (Exception e) {
                    showError("Invalid play: that card would exceed 50.");
                }
            });
        });
    }

    private void playHumanCard(HumanPlayer human, Card card) throws Exception {

        human.playCard(card, gameModel.getTablePile());

        totalMoves++;

        selectedCard = null;
        clearHighlights();
        humanTurn = false;

        updateView();
        startDrawCardThread(human);
    }



    private void startDrawCardThread(Player player) {
        int machineIndex = gameModel.getCurrentPlayerIndex();
        DrawCardThread drawThread = new DrawCardThread(gameModel, player, () -> {
            if (player instanceof MachinePlayer) {
                List<ImageView> views = getMachineCardViews(machineIndex);
                if (views != null) {
                    int newCardIndex = player.getHand().size() - 1;
                    if (newCardIndex >= 0 && newCardIndex < views.size()) {
                        views.get(newCardIndex).setVisible(true);
                        AnimationUtil.fadeInCard(views.get(newCardIndex));
                    }
                }
            }
            updateView();
            checkEliminationAndNextTurn();
        });
        drawThread.start();
    }

    private void checkEliminationAndNextTurn() {
        Player winner = gameModel.checkWinner();
        if (winner != null) {
            showWinner(winner);
            return;
        }
        try {
            gameModel.nextTurn();
            Player current = gameModel.getCurrentPlayer();
            if (!current.hasValidPlay(gameModel.getTablePile().getCurrentSum())) {
                try {
                    AnimationUtil.eliminatedShake(playerIcons.get(gameModel.getCurrentPlayerIndex()));
                    gameModel.eliminateCurrentPlayer();
                } catch (EmptyDeckException e) {
                    showError("Deck error: " + e.getMessage());
                }
                updateView();
                checkEliminationAndNextTurn();
                return;
            }
            updateView();
            if (gameModel.getCurrentPlayer() instanceof MachinePlayer) {
                startMachineTurn();
            } else {
                humanTurn = true;
            }
        } catch (GameStateException e) {
            showError("Game state error: " + e.getMessage());
        }
    }

    private void startMachineTurn() {
        int machineIndex = gameModel.getCurrentPlayerIndex();
        List<ImageView> currentMachineViews = getMachineCardViews(machineIndex);

        MachinePlayerThread machineThread = new MachinePlayerThread(gameModel, (cardIndex) -> {
            if (currentMachineViews != null && cardIndex >= 0 && cardIndex < currentMachineViews.size()) {
                machineAnimationInProgress = true;
                AnimationUtil.playCardToTable(currentMachineViews.get(cardIndex), () -> {

                    totalMoves++;
                    machineAnimationInProgress = false;
                    updateView();
                    startDrawCardThread(gameModel.getCurrentPlayer());
                });
            } else {
                updateView();
                startDrawCardThread(gameModel.getCurrentPlayer());
            }
        });
        machineThread.start();
    }

    private List<ImageView> getMachineCardViews(int playerIndex) {
        return switch (playerIndex) {
            case 1 -> ia1CardViews;
            case 2 -> ia2CardViews;
            case 3 -> ia3CardViews;
            default -> null;
        };
    }

    private void updateView() {
        Platform.runLater(() -> {
            updateTablePile();
            updateCounter();
            updateHumanCards();
            updateMachineCards();
            updatePlayerLabels();
            highlightCurrentPlayer();
        });
    }

    private void updateTablePile() {
        Card top = gameModel.getTablePile().getTopCard();
        if (top != null) {
            discardPile.setImage(loadImage(top.getImagePath()));
            AnimationUtil.fadeInCard(discardPile);
        }
    }

    private void updateCounter() {
        int sum = gameModel.getTablePile().getCurrentSum();
        counterLabel.setText(sum + "/");
    }

    private void updateHumanCards() {
        List<Card> hand = gameModel.getPlayers().get(0).getHand();
        for (int i = 0; i < humanCardViews.size(); i++) {
            if (i < hand.size()) {
                humanCardViews.get(i).setImage(loadImage(hand.get(i).getImagePath()));
                humanCardViews.get(i).setVisible(true);
            } else {
                humanCardViews.get(i).setVisible(false);
            }
        }
    }

    private void updateMachineCards() {
        if (machineAnimationInProgress) return;
        String backPath = "/com/example/miniproyecto3/Imagenes/reverse.png";
        List<List<ImageView>> machineViews = List.of(ia1CardViews, ia2CardViews, ia3CardViews);
        for (int m = 0; m < machineCount; m++) {
            Player machine = gameModel.getPlayers().get(m + 1);
            List<ImageView> views = machineViews.get(m);
            if (machine.isEliminated()) {
                views.forEach(v -> v.setVisible(false));
                continue;
            }
            for (int i = 0; i < GameConstants.HAND_SIZE; i++) {
                if (i < machine.getHand().size()) {
                    views.get(i).setImage(loadImage(backPath));
                    views.get(i).setVisible(true);
                }
            }
        }
    }


    private void updatePlayerLabels() {
        List<Label> labels = List.of(pointsPj1, pointsPj2, pointsPj3, pointsPj4);
        List<Player> players = gameModel.getPlayers();
        for (int i = 0; i < labels.size(); i++) {
            if (i < players.size()) {
                Player p = players.get(i);
                String status = p.isEliminated() ? " ✗" : "";
                labels.get(i).setText(p.getName() + ": " + p.getTotalPoints() + "pts" + status);
                labels.get(i).setVisible(true);
            } else {
                labels.get(i).setVisible(false);
            }
        }
    }

    private void selectCardByIndex(int index) {
        if (!humanTurn) return;
        List<Card> hand = gameModel.getPlayers().get(0).getHand();
        if (index < hand.size()) {
            cardClickHandler.onCardClicked(hand.get(index), index);
        }
    }

    private void highlightSelectedCard(int index) {
        clearHighlights();
        humanCardViews.get(index).getStyleClass().add("card-selected");
        AnimationUtil.selectCardPulse(humanCardViews.get(index));
    }

    private void clearHighlights() {
        for (ImageView card : humanCardViews) {
            card.getStyleClass().remove("card-selected");
            AnimationUtil.deselectCard(card);
        }
    }
    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showWinner(Player winner) {
        Platform.runLater(() -> {
            try {
                MusicManager.stopMusic();
                // Cierra la ventana del juego
                javafx.stage.Stage gameStage =
                        (javafx.stage.Stage) moreBtn.getScene().getWindow();
                gameStage.close();

                // Abre la pantalla de fin de juego
                EndStage endStage = new EndStage();
                endStage.getController().initEndGame(winner, machineCount, totalMoves);

            } catch (IOException e) {
                e.printStackTrace();
                // Fallback: Alert básico si falla la carga del FXML
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setContentText(winner.getName() + " wins!");
                alert.showAndWait();
            }
        });
    }
}