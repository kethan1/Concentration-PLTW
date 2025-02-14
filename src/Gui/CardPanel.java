package Gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Board.Board;

public class CardPanel extends JPanel {
    private Board board;
    private Card[][] cardViews;
    
    private JLabel timerLabel;
    private JLabel bestTimeLabel;
    private Timer gameTimer;
    private int elapsedTime;
    private static int bestTime = Integer.MAX_VALUE;

    public CardPanel(Board board) {
        this.board = board;
        int rows = board.getRows();
        int cols = board.getColumns();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        timerLabel = new JLabel("Time: 0 sec");
        bestTimeLabel = new JLabel("Best Time: " + (bestTime == Integer.MAX_VALUE ? "N/A" : bestTime + " sec"));
        infoPanel.add(timerLabel);
        infoPanel.add(bestTimeLabel);
        add(infoPanel, BorderLayout.NORTH);
        
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        cardViews = new Card[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = new Card(board, row, col, this::cardClicked);
                cardViews[row][col] = card;
                gridPanel.add(card);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        
        elapsedTime = 0;
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                timerLabel.setText("Time: " + elapsedTime + " sec");
            }
        });
        gameTimer.start();
    }

    private void cardClicked(Card card) {
        Board.MoveResult result = board.selectTile(card.getRow(), card.getColumn());
        if (result == null) return;

        card.flip(true);

        switch (result.outcome) {
            case WAITING:
                break;
            case MATCH:
                cardViews[result.firstRow][result.firstCol].markMatched();
                cardViews[result.secondRow][result.secondCol].markMatched();

                if (board.allTilesMatched()) {
                    gameTimer.stop();
                    if (elapsedTime < bestTime) {
                        bestTime = elapsedTime;
                        bestTimeLabel.setText("Best Time: " + bestTime + " sec");
                    }

                    int option = JOptionPane.showOptionDialog(
                        this,
                        "You win!\nPlay again?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        null,
                        null
                    );
                    if (option == JOptionPane.YES_OPTION) {
                        playAgain();
                    }
                }
                break;
            case NO_MATCH:
                Timer timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        board.resetLastMove();
                        cardViews[result.firstRow][result.firstCol].flip(false);
                        cardViews[result.secondRow][result.secondCol].flip(false);
                    }
                });
                timer.setRepeats(false);
                timer.start();
                break;
        }
    }
    
    private void playAgain() {
        board.reset(); 
        
        int rows = board.getRows();
        int cols = board.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cardViews[row][col].reset();
            }
        }
        
        elapsedTime = 0;
        timerLabel.setText("Time: " + elapsedTime + " sec");
        gameTimer.restart();
    }
}
