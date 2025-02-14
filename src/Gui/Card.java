package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import Board.Board;
import Board.Tile;

public class Card extends JButton {
    private int row;
    private int col;
    private Board board;

    private boolean animatingFlip = false;
    private double flipProgress = 1.0;
    private boolean targetFaceUp = false;
    private boolean textSwapped = false;
    private Timer flipTimer;

    public interface CardClickHandler {
        void onClick(Card card);
    }

    public Card(Board board, int row, int col, CardClickHandler handler) {
        this.board = board;
        this.row = row;
        this.col = col;

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setBackground(Color.LIGHT_GRAY);

        addActionListener(e -> {
            Tile tile = board.getTile(row, col);
            if (!tile.matched() && !tile.isFaceUp() && !animatingFlip) {
                handler.onClick(this);
            }
        });
    }

    public void reset() {
        setText("");
        setBackground(Color.LIGHT_GRAY);
        flipProgress = 1.0;
        animatingFlip = false;
        targetFaceUp = false;
        textSwapped = false;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public boolean isFaceUp() {
        return board.getTile(row, col).isFaceUp();
    }

    public boolean isMatched() {
        return board.getTile(row, col).matched();
    }

    public void markMatched() {
        board.getTile(row, col).foundMatch();

        setBackground(Color.GREEN);
    }

    public void flip(boolean showFace) {
        if (animatingFlip) return;
        targetFaceUp = showFace;
        textSwapped = false;
        flipProgress = 0.0;
        animatingFlip = true;

        if (!showFace) {
            setText("");
        }

        flipTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipProgress += 0.05;
                if (flipProgress >= 1.0) {
                    flipProgress = 1.0;
                    animatingFlip = false;
                    board.getTile(row, col).flip(targetFaceUp);
                    flipTimer.stop();
                }
                if (flipProgress >= 0.5 && !textSwapped) {
                    textSwapped = true;

                    if (targetFaceUp) {
                        setText(board.getTileValue(row, col));
                    } else {
                        setText("");
                    }
                }
                repaint();
            }
        });
        flipTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = 20;

        double scale = 1.0;
        if (animatingFlip) {
            scale = (flipProgress < 0.5) ? (1 - 2 * flipProgress) : (2 * flipProgress - 1);
        }

        g2.translate(w / 2, h / 2);
        g2.scale(scale, 1);
        g2.translate(-w / 2, -h / 2);

        RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, w, h, arc, arc);
        g2.setColor(getBackground());
        g2.fill(rect);

        String text = getText();
        if (text != null && !text.isEmpty() && scale > 0.01) {
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            int x = (w - textWidth) / 2;
            int y = (h + textHeight) / 2 - fm.getDescent();
            g2.drawString(text, x, y);
        }
        g2.dispose();
    }
}
