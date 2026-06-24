# Cincuentazo (Miniproyecto 3 — AVS & MAPS)

A **"Cincuentazo"** card game developed in **Java 17** with **JavaFX**, applying Object-Oriented Programming principles (encapsulation, inheritance, polymorphism, interfaces, and custom exception handling).

The player competes against 1, 2, or 3 AI-controlled players. On each turn, the value of the played card is added to a shared counter; whoever has no valid plays left before exceeding the limit is eliminated. The last player standing wins.

---

## Authors

| Name | GitHub | Studen ID |
|---|---|--|
| Alejandro Valencia Sandoval | [@AlejandroVS-code](https://github.com/AlejandroVS-code) | 2515411-2724|
| Maria Alejandra Pizarro Sarria | [@Alejapizasar](https://github.com/Alejapizasar) | 2519474-2724 |

---

## Table of contents

- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [How to run the project](#how-to-run-the-project)
- [Game rules](#game-rules)
- [Project structure](#project-structure)
- [Architecture and OOP principles applied](#architecture-and-oop-principles-applied)
- [Audio and effects](#audio-and-effects)
- [Results screen](#results-screen)
- [Tests](#tests)
- [Authors](#authors)

---

## Technologies

| Technology | Version |
|---|---|
| Java | 17 |
| JavaFX (controls, fxml) | 17.0.14 |
| Maven | (build manager) |
| JUnit Jupiter | 5.12.1 |

---

## Prerequisites

- **JDK 17** or higher installed and configured in the `PATH`.
- **Maven** (or use the included wrapper `mvnw` / `mvnw.cmd`, which requires no prior installation).
- A Maven- and JavaFX-compatible IDE (recommended: IntelliJ IDEA).

---

## How to run the project

### Option 1 — From the terminal with Maven

```bash
# Clone the repository
git clone <repository-url>
cd Miniproyecto-3-50zo-AVS-MAPS

# Run with the JavaFX plugin
mvn clean javafx:run
```

### Option 2 — From the IDE (IntelliJ IDEA)

1. Open the project as a Maven project (`pom.xml`).
2. Wait for the dependencies to be downloaded.
3. Run the `com.example.miniproyecto3.Main` class.

### Compile without running

```bash
mvn clean compile
```

### Run the unit tests

```bash
mvn test
```

---

## Game rules

1. The game is played with a standard 52-card deck (jokers are not counted, although the card image set includes them as a visual asset).
2. Each player (human and machines) is dealt **4 cards** at the start of the game.
3. There is a **shared counter** that starts at the value of the first card revealed from the deck.
4. On their turn, each player plays a card from their hand; its value is added to the counter:

| Card | Value |
|---|---|
| 2 – 8 | Its numeric value (2 to 8) |
| 9 | 0 (neutral) |
| 10 | 10 |
| J, Q, K | -10 |
| Ace | 10 or 1, chosen by the player (via dialog) |

5. After playing, the player draws a new card from the deck to keep 4 cards in hand.
6. **The counter can never exceed 50.** If a player has no card in hand that keeps the sum at 50 or below, they are **eliminated**: their cards are returned to the deck and recycled.
7. The game continues until **only one active player remains**: that player is the **winner**.

---

## Project structure

```
src/main/java/com/example/miniproyecto3/
├── Main.java                      # Application entry point
├── controller/                    # JavaFX controllers (FXML <-> view logic)
│   ├── HomeController.java        # Home screen / AI selection
│   ├── GameController.java        # Game screen
│   └── EndController.java         # Results screen
├── model/                         # Business logic (UI-independent)
│   ├── GameModel.java             # Orchestrates the match (turns, winner, elimination)
│   ├── Player.java                # Abstract base player class
│   ├── HumanPlayer.java           # Human player
│   ├── MachinePlayer.java         # AI player with phase-based strategy
│   ├── Card.java / Deck.java      # Card and deck
│   ├── TablePile.java             # Central pile with the accumulated counter
│   ├── GameConstants.java         # Game configuration constants
│   ├── enums/                     # Rank, Suit, PlayerType
│   ├── interfaces/                # IPlayable, IEliminable, IDeckOperations, ICardClickHandler
│   └── exceptions/                # Custom business exceptions
├── thread/                        # Threads for AI turns and card drawing
├── util/                          # MusicManager, AnimationUtil, DialogUtil
└── View/                          # JavaFX Stages (Home, Game, End)

src/main/resources/com/example/miniproyecto3/
├── Audio/      # Background music and sound effects (click, hover, turn)
├── Cartas/     # Images of the 52 cards + jokers
├── Imagenes/   # Backgrounds, player icons, card back
├── Styles/     # CSS for each screen
└── Vistas/     # FXML files (HomeView, GameView, EndView)

src/test/java/   # Unit tests (Card, Deck, GameModel)
```

---

## Architecture and OOP principles applied

- **Encapsulation:** the attributes of `Player`, `Card`, `TablePile`, etc. are private/protected and exposed through controlled getters; `GameConstants` uses a private constructor to prevent instantiation.
- **Inheritance:** `HumanPlayer` and `MachinePlayer` inherit from the abstract class `Player`, reusing common logic (score, hand, elimination) while specializing the actual play behavior.
- **Polymorphism:** `GameModel` and `GameController` operate on `Player` references without knowing, at compile time, whether the concrete implementation is human or machine; the `playCard(...)` method behaves differently depending on the subclass. The results screen also relies on this: it compares `winner.getPlayerType()` to decide whether to show victory or defeat, regardless of which concrete subclass won.
- **Interfaces (contracts):**
    - `IPlayable` — defines the contract for playing and drawing a card.
    - `IEliminable` — defines the contract for eliminating a player.
    - `IDeckOperations` — contract for deck operations.
    - `ICardClickHandler` — contract for handling card click events (view layer).
- **Custom exceptions:** `EmptyDeckException`, `GameStateException`, and `InvalidCardPlayException` (the latter also encapsulates the card's value and the current sum at the moment of the error) allow business errors to be distinguished and handled explicitly, instead of relying on generic exceptions.
- **Separation of concerns (MVC pattern):**
    - **Model:** game rules, completely independent of JavaFX.
    - **View:** FXML + CSS files.
    - **Controller:** connects UI events to the model and updates the view.
- **Concurrency:** `MachinePlayerThread` and `DrawCardThread` run the AI turns and card draws on separate threads, using `Platform.runLater(...)` to synchronize visual updates with the JavaFX thread.

---

## Audio and effects

### Background music
A different track per screen, looped continuously, managed by `MusicManager.playMusic(...)`:

| Screen | File |
|---|---|
| Main menu | `menu.wav` |
| Game in progress | `game.wav` |
| Results | `end.wav` |

### Short sound effects
Played with `MusicManager.playSoundEffect(...)` (no loop, on an independent `Clip` that does not interrupt the background music):

| Effect | File | Where it triggers |
|---|---|---|
| Hover | `hover.wav` | When hovering over buttons (`HomeController`, `GameController`, `EndController`) and over the human player's cards during their turn |
| Click | `click.wav` | When pressing any button with `AnimationUtil.addPressEffect(...)` |
| Turn | `turn.wav` | Every time the human player's turn begins, alongside the icon highlight animation |

The volume of the background music and of the sound effects are controlled by **independent** constants in `MusicManager` (`DEFAULT_VOLUME` and `SFX_VOLUME`), so they can be adjusted separately.

### Animations (`AnimationUtil`)
- Hover and press effects on buttons (scale + `Glow` highlight).
- Active turn highlight (`highlightTurn`).
- Card selection, play, draw, and elimination (pulse, fade, move to pile/deck, shake).

---

## Results screen

When the game ends, `EndController` receives the winning player and compares their type (`PlayerType`) against `HUMAN`:

- **If the human player wins:** **"VICTORIA"** is displayed in gold.
- **If an AI wins:** **"¡FALLASTE!"** is displayed in red, along with the name and score of the winning AI.

In both cases, the final score and total moves of the winner are also shown. The **Menu** and **New Game** buttons on this screen have the same hover and click effects as the rest of the application.

---

## Tests

The project includes JUnit 5 unit tests in `src/test/java/`:

- `CardTest.java` — validates card values and behavior.
- `DeckTest.java` — deck operations (draw, recycle, shuffle).
- `GameModelTest.java` — turn flow, elimination, and winner determination.

```bash
mvn test
```