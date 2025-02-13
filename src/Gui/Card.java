package Gui;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

import Board.Board;

public class Card extends JButton {
    private Board board;
    private int column;
    private int row;
    private boolean isMatched = false;

    public static interface ButtonClickHandler {
        void action(Card card);
    }

    public Card(Board board, int column, int row, ButtonClickHandler handler) {
        this.board = board;
        this.column = column;
        this.row = row;

        setBorder(new RoundedBorder(10));
        setBackground(Color.LIGHT_GRAY);
        setVisible(true);

        addActionListener(e -> {
            if (!isMatched) {
                setText(board.get(row, column));
                board.show(row, column);
                handler.action(this);
            }
        });
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void isMatched() {
        board.getTile(row, column).matched();
        setBackground(Color.GREEN);
    }

    public void flip(boolean isFlipped) {
        if (isFlipped) {
            setText(board.get(row, column));
        } else {
            setText("");
        }
    }
}

class RoundedBorder implements Border {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}