import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.Deck;
import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;
import com.example.miniproyecto3.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testDeckInitialSize() {
        assertEquals(52, deck.size());
    }

    @Test
    public void testDeckIsNotEmptyAfterInit() {
        assertFalse(deck.isEmpty());
    }

    @Test
    public void testDrawReducesSize() throws EmptyDeckException {
        int initialSize = deck.size();
        deck.draw();
        assertEquals(initialSize - 1, deck.size());
    }

    @Test
    public void testDrawReturnsCard() throws EmptyDeckException {
        Card card = deck.draw();
        assertNotNull(card);
    }

    @Test
    public void testDrawAllCards() throws EmptyDeckException {
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertTrue(deck.isEmpty());
    }

    @Test
    public void testDrawFromEmptyDeckThrowsException() throws EmptyDeckException {
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        assertThrows(EmptyDeckException.class, () -> deck.draw());
    }

    @Test
    public void testRecycleIncreasesSize() throws EmptyDeckException {
        List<Card> cards = new ArrayList<>();
        cards.add(deck.draw());
        cards.add(deck.draw());
        cards.add(deck.draw());
        int sizeAfterDraw = deck.size();
        deck.recycle(cards);
        assertEquals(sizeAfterDraw + 3, deck.size());
    }

    @Test
    public void testRecycleFromEmptyDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(Rank.ACE, Suit.SPADES));
        cards.add(new Card(Rank.KING, Suit.HEARTS));

        Deck emptyDeck = new Deck();
        try {
            for (int i = 0; i < 52; i++) emptyDeck.draw();
        } catch (EmptyDeckException e) {
            fail("Should not throw here");
        }

        emptyDeck.recycle(cards);
        assertFalse(emptyDeck.isEmpty());
    }

    @Test
    public void testShuffleDoesNotChangeDeckSize() {
        int sizeBefore = deck.size();
        deck.shuffle();
        assertEquals(sizeBefore, deck.size());
    }
}