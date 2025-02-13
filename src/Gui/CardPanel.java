package Gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Board.Board;

public class CardPanel extends JPanel {
    private Board board;
    // Store Card views in a 2D array so we can easily update them.
    private Card[][] cardViews;

    public CardPanel(Board board) {
        this.board = board;
        int rows = board.getRows();
        int cols = board.getColumns();
        setLayout(new GridLayout(rows, cols, 10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        cardViews = new Card[rows][cols];
        // Create a Card for each Tile on the board.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = new Card(board, row, col, this::cardClicked);
                cardViews[row][col] = card;
                add(card);
            }
        }
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
                    JOptionPane.showMessageDialog(this, "You win!");
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
}
