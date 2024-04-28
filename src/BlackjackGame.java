import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BlackjackGame {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;

    final JFrame frame;
    final JPanel playerPanel;
    final JPanel dealerPanel;
    final JTextArea playerHandTextArea;
    final JLabel playerScoreLabel;
    final JTextArea dealerHandTextArea;
    final JLabel dealerScoreLabel;
    final JButton hitButton;
    final JButton standButton;

    public BlackjackGame() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();

        // GUI stuff that I definitely didn't have to search up how to do
        frame = new JFrame("Blackjack game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        playerPanel = new JPanel(new BorderLayout());
        dealerPanel = new JPanel(new BorderLayout());

        // The border stuff for the hand panels
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        playerPanel.setBorder(BorderFactory.createTitledBorder(border, "Player"));
        dealerPanel.setBorder(BorderFactory.createTitledBorder(border, "Dealer"));

        playerHandTextArea = new JTextArea(5, 20);
        playerHandTextArea.setEditable(false);
        playerHandTextArea.setLineWrap(true);
        JScrollPane playerScrollPane = new JScrollPane(playerHandTextArea);

        dealerHandTextArea = new JTextArea(5, 20);
        dealerHandTextArea.setEditable(false);
        dealerHandTextArea.setLineWrap(true);
        JScrollPane dealerScrollPane = new JScrollPane(dealerHandTextArea);

        playerPanel.add(playerScrollPane, BorderLayout.CENTER);
        dealerPanel.add(dealerScrollPane, BorderLayout.CENTER);

        playerScoreLabel = new JLabel("Score: ");
        dealerScoreLabel = new JLabel("Score: ");

        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.add(playerScoreLabel, BorderLayout.NORTH);

        JPanel dealerInfoPanel = new JPanel(new BorderLayout());
        dealerInfoPanel.add(dealerScoreLabel, BorderLayout.NORTH);

        playerPanel.add(playerInfoPanel, BorderLayout.SOUTH);
        dealerPanel.add(dealerInfoPanel, BorderLayout.SOUTH);

        hitButton = new JButton("Hit");
        hitButton.addActionListener(_ -> {
            playerHand.addCard(deck.drawCard());
            updateGUI();
            if (playerHand.calculateScore() > 21) {
                hitButton.setEnabled(false);
                dealerTurn();
                determineWinner();
            }
        });

        standButton = new JButton("Stand");
        standButton.addActionListener(_ -> {
            dealerTurn();
            determineWinner();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        frame.add(playerPanel, BorderLayout.WEST);
        frame.add(dealerPanel, BorderLayout.EAST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        dealInitialCards();
        updateGUI();

        frame.pack();
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private void dealInitialCards() {
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
    }

    private void updateGUI() {
        playerHandTextArea.setText(playerHand.toString());
        playerScoreLabel.setText("Score: " + playerHand.calculateScore());
        dealerHandTextArea.setText(dealerHand.toString());
        dealerScoreLabel.setText("Score: " + dealerHand.calculateScore());
    }

    private void dealerTurn() {
        while (dealerHand.calculateScore() < playerHand.calculateScore()) {
            dealerHand.addCard(deck.drawCard());
            updateGUI();
        }
    }

    private void determineWinner() {
        int playerScore = playerHand.calculateScore();
        int dealerScore = dealerHand.calculateScore();

        String resultMessage;
        if (playerScore > 21) {
            resultMessage = "Bust! You lose. \uD83D\uDE21";
        } else if (dealerScore > 21 || playerScore > dealerScore) {
            resultMessage = "You win! \uD83D\uDE03";
        } else if (playerScore == dealerScore) {
            resultMessage = "It's a tie. \uD83D\uDE36";
        } else {
            resultMessage = "Dealer wins. \uD83D\uDE21";
        }

        // Shows result in new window panel
        JOptionPane.showMessageDialog(frame, resultMessage, "Game Result", JOptionPane.INFORMATION_MESSAGE);

        // Restart the game
        restartGame();
    }

    private void restartGame() {
        //Reset deck
        deck = new Deck();

        // Reset hands
        playerHand = new Hand();
        dealerHand = new Hand();

        dealInitialCards();

        hitButton.setEnabled(true);

        updateGUI();
    }

    public static void main(String[] args) {
        new BlackjackGame();
    }
}