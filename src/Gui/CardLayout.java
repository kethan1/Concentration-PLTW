package Gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Board.Board;

class FutureListener implements ActionListener {
    Card prevCard;
    Card card;

    FutureListener(Card prevCard, Card card) {
        this.prevCard = prevCard;
        this.card = card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        prevCard.flip(false);
        card.flip(false);
    }
}

public class CardLayout extends JPanel {
    private Board board;
    private GridLayout layout;
    private Card prevCard;
    private int cardFlips;

    public CardLayout(Board board) {
        this.board = board;

        layout = new GridLayout(getRows(), getColumns());
        layout.setVgap(25);
        layout.setHgap(25);

        setLayout(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        setVisible(true);

        for (int c = 0; c < getColumns(); c++) {
            for (int r = 0; r < getRows(); r++) {
                add(new Card(board, c, r, (Card card) -> {
                    cardFlips++;
                    if (cardFlips % 2 == 0 && prevCard != null) {
                        if (board.checkForMatch(prevCard.getRow(), prevCard.getColumn(), card.getRow(),
                                card.getColumn())) {
                            prevCard.isMatched();
                            card.isMatched();
                            Timer timer = new Timer(300, new FutureListener(prevCard, card));
                            timer.setRepeats(false);
                            timer.start();
                        } else {
                            Timer timer = new Timer(500, new FutureListener(prevCard, card));
                            timer.setRepeats(false);
                            timer.start();
                        }

                        prevCard = null;
                    } else {
                        prevCard = card;
                    }
                }), r, c);
            }
        }
    }

    public int getColumns() {
        return board.getColumns();
    }

    public int getRows() {
        return board.getRows();
    }
}
