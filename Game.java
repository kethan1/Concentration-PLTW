
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

/**
 * A game class to play concentration
 */
public class Game {
  private Scanner in = new Scanner(System.in);

  private Board board;

  public void play() {
    // instructions
    System.out.println("Welcome!");
    System.out.println("Select the tile locations you want to match,");
    System.out.println("or enter any non-integer character to quit.");
    System.out.println("(You will need to know 2D arrays to play!)");
    System.out.println("\nPress Enter to continue...");
    in.nextLine();

    board = new Board();
    // play until all tiles are matched
    while (!board.allTilesMatch()) {
      // get player's first selection, if not an integer, quit
      int row1 = -1;
      int col1 = -1;
      boolean validTile = false;
      while (!validTile) {
        displayBoard();
        System.out.print("First choice (row col): ");
        UserSelection selection = getTile();

        validTile = selection.isValid();
        row1 = selection.getRow();
        col1 = selection.getColumn();
      }

      // display first tile
      board.showValue(row1, col1);

      // get player's second selection, if not an integer, quit
      int row2 = -1;
      int col2 = -1;
      validTile = false;
      while (!validTile) {
        displayBoard();
        System.out.print("Second choice (row col): ");

        UserSelection selection = getTile();

        validTile = selection.isValid();
        row2 = selection.getRow();
        col2 = selection.getColumn();

        // check if user chosen same tile twice
        if ((row1 == row2) && (col1 == col2)) {
          System.out.println("You mush choose a different second tile");
          wait(2);
          validTile = false;
        }
      }

      // display second tile
      board.showValue(row2, col2);
      displayBoard();

      // determine if tiles match
      String matched = board.checkForMatch(row1, col1, row2, col2);
      System.out.println(matched);

      board.hideAll();

      // wait 2 seconds to start the next turn
      wait(2);
    }

    displayBoard();

    System.out.println("Game Over!");
  }

  /**
   * Get tile choices from the user, validating that
   * the choice falls within the range of rows and columns on the board.
   *
   * @return UserSelection object
   */

  private UserSelection getTile() {
    int row = 0;
    int column = 0;

    if (in.hasNextInt())
      row = in.nextInt();
    else
      quitGame();

    if (in.hasNextInt())
      column = in.nextInt();
    else
      quitGame();

    in.reset(); // ignore any extra input

    if (!board.validateSelection(row, column)) {
      System.out.print("Invalid input, please try again. ");
      wait(2);
      return new UserSelection();
    }

    return new UserSelection(row, column);
  }

  /**
   * Clear the console and show the game board
   */
  public void displayBoard() {

    // scroll current board off screen
    for (int x = 0; x < 50; x++) {
      System.out.println();
    }

    System.out.println(board);
  }

  /**
   * Wait n seconds before clearing the console
   */
  private void wait(int n) {
    try {
      Thread.sleep(n * 1000);
    } catch (InterruptedException e) {}
  }

  /**
   * User quits game
   */
  private void quitGame() {
    System.out.println("Quit game!");
    System.exit(0);
  }
}
