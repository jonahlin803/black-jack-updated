import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

record Card(String suit, String rank) {

    public int getValue() {
        if (rank.equals("A")) {
            return 11;
        } else if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.removeFirst();
    }
}

class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int calculateScore() {
        int score = 0;
        int numAces = 0;
        for (Card card : cards) {
            score += card.getValue();
            if (card.rank().equals("A")) {
                numAces++;
            }
        }
        while (numAces > 0 && score > 21) {
            score -= 10;
            numAces--;
        }
        return score;
    }

    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card).append("\n");
        }
        return handString.toString();
    }
}