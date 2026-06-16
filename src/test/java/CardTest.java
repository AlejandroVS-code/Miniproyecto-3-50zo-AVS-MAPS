import com.example.miniproyecto3.model.Card;
import com.example.miniproyecto3.model.enums.Rank;
import com.example.miniproyecto3.model.enums.Suit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void testNumberCardValue() {
        Card card = new Card(Rank.FIVE, Suit.HEARTS);
        assertEquals(5, card.getValue(0));
    }

    @Test
    public void testNineValueIsZero() {
        Card card = new Card(Rank.NINE, Suit.CLUBS);
        assertEquals(0, card.getValue(30));
    }

    @Test
    public void testFaceCardValueIsMinusTen() {
        Card jack = new Card(Rank.JACK, Suit.SPADES);
        Card queen = new Card(Rank.QUEEN, Suit.DIAMONDS);
        Card king = new Card(Rank.KING, Suit.HEARTS);
        assertEquals(-10, jack.getValue(0));
        assertEquals(-10, queen.getValue(0));
        assertEquals(-10, king.getValue(0));
    }

    @Test
    public void testAceValueTenWhenSumIsLow() {
        Card ace = new Card(Rank.ACE, Suit.SPADES);
        assertEquals(10, ace.getValue(20));
    }

    @Test
    public void testAceValueOneWhenSumIsHigh() {
        Card ace = new Card(Rank.ACE, Suit.SPADES);
        assertEquals(1, ace.getValue(45));
    }

    @Test
    public void testAceValueTenWhenSumIsExactly40() {
        Card ace = new Card(Rank.ACE, Suit.HEARTS);
        assertEquals(10, ace.getValue(40));
    }

    @Test
    public void testAceValueOneWhenSumIsExactly41() {
        Card ace = new Card(Rank.ACE, Suit.HEARTS);
        assertEquals(1, ace.getValue(41));
    }

    @Test
    public void testGetImagePath() {
        Card card = new Card(Rank.JACK, Suit.SPADES);
        assertEquals("/com/example/miniproyecto3/Cartas/jack_of_spades.png",
                card.getImagePath());
    }

    @Test
    public void testToString() {
        Card card = new Card(Rank.THREE, Suit.DIAMONDS);
        assertEquals("three of diamonds", card.toString());
    }
}