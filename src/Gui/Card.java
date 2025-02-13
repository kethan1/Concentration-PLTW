package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import Board.Board;

public class Card extends JButton {
    private Board board;
    private int column;
    private int row;
    private boolean isMatched = false;
    private boolean faceUp = false;

    private boolean animatingFlip = false;
    private double flipProgress = 1.0;
    private boolean targetFaceUp = false;
    private boolean textSwapped = false;
    private Timer flipTimer;
    
    public static interface ButtonClickHandler {
        void action(Card card);
    }
    
    public Card(Board board, int column, int row, ButtonClickHandler handler) {
        this.board = board;
        this.column = column;
        this.row = row;
        
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        setBackground(Color.LIGHT_GRAY);
        
        addActionListener(e -> {
            if (!isMatched && !faceUp && !animatingFlip) {
                flip(true); // animate flipping to show the card’s face
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
        isMatched = true;
    }
    
    public void flip(boolean isFlipped) {
        if (animatingFlip) return;

        System.out.println("flipping");
        
        targetFaceUp = isFlipped;
        textSwapped = false;
        flipProgress = 0.0;
        animatingFlip = true;
        
        if (!isFlipped) {
            setText(board.get(row, column));
        }
        
        flipTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flipProgress += 0.05;
                if (flipProgress >= 1.0) {
                    flipProgress = 1.0;
                    animatingFlip = false;
                    faceUp = targetFaceUp;
                    flipTimer.stop();
                }

                if (flipProgress >= 0.5 && !textSwapped) {
                    textSwapped = true;
                    if (targetFaceUp) {
                        setText(board.get(row, column));
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
        
        RoundRectangle2D.Double cardRect = new RoundRectangle2D.Double(0, 0, w, h, arc, arc);

        g2.setColor(getBackground());
        g2.fill(cardRect);
        
        String text = getText();
        if (text != null && !text.isEmpty() && scale > 0.01) {
            g2.setColor(getForeground());
            Font font = getFont();
            g2.setFont(font);
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
