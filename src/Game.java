
/**
 * The game that uses a n x m board of tiles or cards.
 *
 * Player chooses two random tiles from the board. The chosen tiles
 * are temporarily shown face up. If the tiles match, they are removed from board.
 *
 * Play continues, matching two tiles at a time, until all tiles have been matched.
 *
 * @author PLTW
 * @version 2.0
*/
import java.util.Scanner;
import javax.swing.*;

import Board.Board;

import Gui.CardLayout;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * A game class to play concentration
 */
public class Game {
     private Scanner in = new Scanner(System.in);

    private Board board;
    private JFrame frame;

    public void play() {
        try {
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        board = new Board();

        frame = new JFrame("Concentration Game");

        frame.getContentPane().add(new CardLayout(board));
        frame.pack();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
