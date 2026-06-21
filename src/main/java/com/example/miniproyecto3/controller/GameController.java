package com.example.miniproyecto3.controller;

import com.example.miniproyecto3.model.*;
import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import com.example.miniproyecto3.model.exceptions.GameStateException;
import com.example.miniproyecto3.model.interfaces.ICardClickHandler;
import com.example.miniproyecto3.thread.DrawCardThread;
import com.example.miniproyecto3.thread.MachinePlayerThread;
import com.example.miniproyecto3.util.AnimationUtil;
import com.example.miniproyecto3.util.DialogUtil;
import com.example.miniproyecto3.util.MusicManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import com.example.miniproyecto3.view.EndStage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the main game screen (GameView.fxml).
 *
 * Drives the entire match while it is in progress: renders each player's
 * cards and score, handles the human player's card selection and play
 * (mouse and keyboard), triggers AI turns and card draws on background
 * threads, updates the table pile counter, highlights the active player's
 * turn, detects eliminations, and transitions to the results screen once a
 * winner is determined.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */
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
    @FXML private Label deckCountLabel;

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

    private List<ImageView> humanCardViews;
    private List<ImageView> ia1CardViews;
    private List<ImageView> ia2CardViews;
    private List<ImageView> ia3CardViews;

    /**
     * Handles clicks on any of the human player's card views: ignores the
     * click if it is not the human player's turn, otherwise records the
     * clicked card as selected and highlights it.
     */
    private final ICardClickHandler cardClickHandler = new ICardClickHandler() {
        @Override
        public void onCardClicked(Card card, int index) {
            if (!humanTurn) return;
            selectedCard = card;
            highlightSelectedCard(index);
        }
    };

    /**
     * Updates the visual state of every player icon (active, eliminated, or
     * idle) based on the current turn and elimination status, and plays the
     * turn sound effect whenever it becomes the human player's turn.
     */
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
                    if (i == 0) {
                        MusicManager.playSoundEffect("/com/example/miniproyecto3/Audio/turn.wav");
                    }
                }
            }
        }
    }

    /**
     * Called automatically by JavaFX after the FXML fields are injected.
     * Starts the game background music, groups the injected card/icon views
     * into lists for easier iteration, wires the "play card" button and its
     * hover/press effects, and sets up keyboard shortcuts for selecting and
     * playing cards (arrow keys, digits 1-4, Enter, Escape).
     */
    @FXML
    public void initialize() {
        MusicManager.playMusic("/com/example/miniproyecto3/Audio/game.wav");

        humanCardViews = List.of(humanCard1, humanCard2, humanCard3, humanCard4);
        ia1CardViews   = List.of(ia1Card1, ia1Card2, ia1Card3, ia1Card4);
        ia2CardViews   = List.of(ia2Card1, ia2Card2, ia2Card3, ia2Card4);
        ia3CardViews   = List.of(ia3Card1, ia3Card2, ia3Card3, ia3Card4);
        playerIcons    = List.of(humanIcon, ia1Icon, ia2Icon, ia3Icon);

        moreBtn.setOnAction(e -> handlePlayCard());
        humanCardViews.forEach(v -> v.getStyleClass().add("human-card"));

        AnimationUtil.addHoverEffect(moreBtn);
        AnimationUtil.addPressEffect(moreBtn);

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

    /**
     * Moves the current card selection left or right within the human
     * player's hand, wrapping around at either end. Used by the arrow-key
     * keyboard shortcuts.
     *
     * @param direction {@code 1} to move right, {@code -1} to move left.
     */
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

    /**
     * Starts a new match using the given model and number of AI opponents:
     * deals the initial hands, wires the card click/hover handlers, sets the
     * visibility of the AI panels according to {@code machineCount}, and
     * renders the initial state.
     *
     * @param gameModel    the game model to drive this match.
     * @param machineCount number of AI opponents in this match.
     */
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

    /**
     * Wires the click and hover handlers for every card view belonging to
     * the human player. The hover sound effect only plays while it is the
     * human player's turn.
     */
    private void setupCardClickEvents() {
        List<Card> hand = gameModel.getPlayers().get(0).getHand();
        for (int i = 0; i < humanCardViews.size(); i++) {
            final int index = i;
            humanCardViews.get(i).setOnMouseClicked(e -> {
                if (index < hand.size()) {
                    cardClickHandler.onCardClicked(hand.get(index), index);
                }
            });

            humanCardViews.get(i).setOnMouseEntered(e -> {
                if (humanTurn && index < hand.size()) {
                    MusicManager.playSoundEffect("/com/example/miniproyecto3/Audio/hover.wav");
                }
            });
        }
    }

    /**
     * Shows or hides each AI player's icon and card views depending on how
     * many AI opponents were selected for this match.
     */
    private void setupMachineVisibility() {
        ia1Icon.setVisible(machineCount >= 1);
        ia2Icon.setVisible(machineCount >= 2);
        ia3Icon.setVisible(machineCount >= 3);
        ia1CardViews.forEach(v -> v.setVisible(machineCount >= 1));
        ia2CardViews.forEach(v -> v.setVisible(machineCount >= 2));
        ia3CardViews.forEach(v -> v.setVisible(machineCount >= 3));
    }

    /**
     * Handles the "play card" button action for the human player. If the
     * selected card is an Ace, opens the value-selection dialog; otherwise
     * validates the play against the current table sum, shaking the card
     * and showing an error if it would exceed the limit, or animating it
     * onto the discard pile and resolving the play if valid.
     */
    private void handlePlayCard() {
        if (!humanTurn || selectedCard == null) return;
        try {
            HumanPlayer human = (HumanPlayer) gameModel.getPlayers().get(0);
            int index = human.getHand().indexOf(selectedCard);

            if (selectedCard.getRank() == Rank.ACE) {
                askAceValue(human, index);
            } else {
                int cardValue = selectedCard.getValue(gameModel.getTablePile().getCurrentSum());
                if (gameModel.getTablePile().getCurrentSum() + cardValue > GameConstants.MAX_SUM) {
                    AnimationUtil.invalidCardShake(humanCardViews.get(index));
                    DialogUtil.showError("Esa carta excede 50 puntos.");
                    return;
                }

                List<ImageView> cardToAnimate = List.of(humanCardViews.get(index));
                AnimationUtil.sendCardsToDeck(cardToAnimate, discardPile, () -> {
                    try {
                        playHumanCard(human, selectedCard);
                    } catch (Exception e) {
                        showError("Esta carta excede 50 puntos.");
                    }
                });
            }
        } catch (Exception e) {
            showError("carta excede 50 puntos.");
        }
    }

    /**
     * Prompts the human player to choose the value (10 or 1) for an Ace
     * being played, then animates the card onto the discard pile and
     * resolves the play with the chosen value.
     *
     * @param human the human player playing the Ace.
     * @param index the index of the Ace within the player's hand/card views.
     */
    private void askAceValue(HumanPlayer human, int index) {
        int aceValue = DialogUtil.showAceDialog();
        List<ImageView> cardToAnimate = List.of(humanCardViews.get(index));
        AnimationUtil.sendCardsToDeck(cardToAnimate, discardPile, () -> {
            try {
                human.playCardWithValue(selectedCard, gameModel.getTablePile(), aceValue);
                human.addMove();
                selectedCard = null;
                clearHighlights();
                humanTurn = false;
                updateView();
                startDrawCardThread(human);
            } catch (Exception e) {
                DialogUtil.showError("Esa jugada excede 50 puntos.");
            }
        });
    }

    /**
     * Resolves a confirmed, valid play by the human player: applies it to
     * the model, clears the current selection, ends the human player's turn,
     * refreshes the view, and starts the background thread that draws a
     * replacement card.
     *
     * @param human the human player whose card is being played.
     * @param card  the card being played.
     * @throws Exception propagated if the underlying play is rejected by the model.
     */
    private void playHumanCard(HumanPlayer human, Card card) throws Exception {
        human.playCard(card, gameModel.getTablePile());
        human.addMove();
        selectedCard = null;
        clearHighlights();
        humanTurn = false;
        updateView();
        startDrawCardThread(human);
    }

    /**
     * Starts a background DrawCardThread for the given player so
     * the replacement card is drawn without blocking the JavaFX thread. On
     * completion, reveals the newly drawn card if it belongs to an AI
     * player, refreshes the view, and checks for eliminations / advances
     * the turn.
     *
     * @param player the player who needs to draw a replacement card.
     */
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

    /**
     * Checks whether the match has a winner; if so, shows the results
     * screen. Otherwise advances to the next turn, eliminating the new
     * current player if they have no valid play, or starting either the AI
     * turn logic or enabling human input as appropriate.
     */
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
                int eliminatedIndex = gameModel.getCurrentPlayerIndex();
                List<ImageView> eliminatedViews = eliminatedIndex == 0
                        ? humanCardViews
                        : getMachineCardViews(eliminatedIndex);

                AnimationUtil.eliminatedShake(playerIcons.get(eliminatedIndex));

                AnimationUtil.sendCardsToDeck(eliminatedViews, drawDeck, () -> {
                    try {
                        gameModel.eliminateCurrentPlayer();
                    } catch (EmptyDeckException e) {
                        showError("Deck error: " + e.getMessage());
                    }
                    updateView();
                    checkEliminationAndNextTurn();
                });
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

    /**
     * Starts a background MachinePlayerThread to resolve the current
     * AI player's turn: animates the chosen card moving onto the table,
     * updates its move count, refreshes the view, and triggers the
     * replacement card draw.
     */
    private void startMachineTurn() {
        int machineIndex = gameModel.getCurrentPlayerIndex();
        List<ImageView> currentMachineViews = getMachineCardViews(machineIndex);

        MachinePlayerThread machineThread = new MachinePlayerThread(gameModel, (cardIndex) -> {
            if (currentMachineViews != null && cardIndex >= 0 && cardIndex < currentMachineViews.size()) {
                machineAnimationInProgress = true;
                AnimationUtil.playCardToTable(currentMachineViews.get(cardIndex), () -> {
                    gameModel.getPlayers().get(machineIndex).addMove();
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

    /**
     * @param playerIndex the player's index within gameModel.getPlayers().
     * @return the list of card views belonging to the AI player at the given
     *         index, or {@code null} if the index does not correspond to an
     *         AI player (e.g. index 0, the human player).
     */
    private List<ImageView> getMachineCardViews(int playerIndex) {
        return switch (playerIndex) {
            case 1 -> ia1CardViews;
            case 2 -> ia2CardViews;
            case 3 -> ia3CardViews;
            default -> null;
        };
    }

    /**
     * Refreshes every visual element of the game screen (table pile,
     * counter, hands, player labels, and turn highlight) on the JavaFX
     * application thread, since this method may be invoked from background
     * threads.
     */
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

    /**
     * Updates the discard pile image to show the card currently on top, if any.
     */
    private void updateTablePile() {
        Card top = gameModel.getTablePile().getTopCard();
        if (top != null) {
            discardPile.setImage(loadImage(top.getImagePath()));
            AnimationUtil.fadeInCard(discardPile);
        }
    }

    /**
     * Updates the running sum label and deck count label, and applies a CSS
     * style class to the counter reflecting how close the sum is to the
     * maximum allowed value (low, medium, high, danger).
     */
    private void updateCounter() {
        int sum = gameModel.getTablePile().getCurrentSum();
        counterLabel.setText(sum + "/");
        deckCountLabel.setText(gameModel.getDeck().size() + " cartas");
        counterLabel.getStyleClass().removeAll(
                "counter-low", "counter-medium", "counter-high", "counter-danger"
        );
        if (sum <= 20) {
            counterLabel.getStyleClass().add("counter-low");
        } else if (sum <= 35) {
            counterLabel.getStyleClass().add("counter-medium");
        } else if (sum <= 45) {
            counterLabel.getStyleClass().add("counter-high");
        } else {
            counterLabel.getStyleClass().add("counter-danger");
        }
    }

    /**
     * Updates the human player's card images to reflect the current hand,
     * hiding any unused card slots.
     */
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

    /**
     * Updates the AI players' card views to show the card back image for
     * every card still in hand, hiding the views of eliminated AI players.
     * Skipped while a machine card animation is in progress to avoid
     * visual glitches.
     */
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

    /**
     * Updates each player's score label with their current name, total
     * points, and an elimination marker if applicable, hiding labels for
     * player slots not in use.
     */
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

    /**
     * Selects the card at the given index in the human player's hand, used
     * by the digit-key (1-4) keyboard shortcuts.
     *
     * @param index the index of the card to select.
     */
    private void selectCardByIndex(int index) {
        if (!humanTurn) return;
        List<Card> hand = gameModel.getPlayers().get(0).getHand();
        if (index < hand.size()) {
            cardClickHandler.onCardClicked(hand.get(index), index);
        }
    }

    /**
     * Clears any previous card selection highlight and highlights the card
     * at the given index as the current selection.
     *
     * @param index the index of the card to highlight.
     */
    private void highlightSelectedCard(int index) {
        clearHighlights();
        humanCardViews.get(index).getStyleClass().add("card-selected");
        AnimationUtil.selectCardPulse(humanCardViews.get(index));
    }

    /**
     * Removes the selection highlight style and animation from every card
     * in the human player's hand.
     */
    private void clearHighlights() {
        for (ImageView card : humanCardViews) {
            card.getStyleClass().remove("card-selected");
            AnimationUtil.deselectCard(card);
        }
    }

    /**
     * Loads an image from the application's classpath resources.
     *
     * @param path the classpath resource path of the image.
     * @return the loaded Image.
     */
    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    /**
     * Displays an error dialog with the given message on the JavaFX
     * application thread.
     *
     * @param message the error message to display.
     */
    private void showError(String message) {
        Platform.runLater(() -> DialogUtil.showError(message));
    }

    /**
     * Closes the game screen and opens the results screen once a winner has
     * been determined, passing along the winner's data. Falls back to a
     * simple alert dialog if the results screen fails to load.
     *
     * @param winner the player who won the match.
     */
    private void showWinner(Player winner) {
        Platform.runLater(() -> {
            try {
                MusicManager.stopMusic();
                javafx.stage.Stage gameStage =
                        (javafx.stage.Stage) moreBtn.getScene().getWindow();
                gameStage.close();

                EndStage endStage = new EndStage();
                endStage.getController().initEndGame(
                        winner,
                        machineCount,
                        winner.getTotalMoves()
                );

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setContentText(winner.getName() + " wins!");
                alert.showAndWait();
            }
        });
    }
}