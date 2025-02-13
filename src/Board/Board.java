package Board;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

/**
 * A game board of NxM board of tiles.
 */
public class Board {
    private static String[] tileValues = {
            "lion", "lion",
            "penguin", "penguin",
            "dolphin", "dolphin",
            "fox", "fox",
            "monkey", "monkey",
            "turtle", "turtle",
    };
    private Tile[][] gameboard = new Tile[3][4];

    /**
     * Constructor for the game. Creates the 2D gameboard and populates it with card
     * values.
     */
    public Board() throws IllegalArgumentException {
        if (gameboard.length * gameboard[0].length != tileValues.length || tileValues.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid gameboard dimensions or tile values");
        }

        ArrayList<String> cTileValues = new ArrayList<>(Arrays.asList(tileValues));

        Collections.shuffle(cTileValues);

        int tileVal = 0;

        for (int c = 0; c < gameboard.length; c++) {
            for (int r = 0; r < gameboard[0].length; r++) {
                gameboard[c][r] = new Tile(cTileValues.get(tileVal++));
            }
        }
    }

    /**
     * Returns a string representation of the board, getting the state of
     * each tile. If the tile is showing, displays its value,
     * otherwise displays it as hidden.
     * 
     * Precondition: gameboard is populated with tiles
     * 
     * @return a string represetation of the board
     */
    public String toString() {
        String gameboardStr = "";

        for (int c = 0; c < gameboard.length; c++) {
            Tile[] row = gameboard[c];
            for (int r = 0; r < gameboard[c].length; r++) {
                Tile elem = row[r];

                if (!elem.isShowingValue()) {
                    gameboardStr += "HIDDEN";
                } else {
                    gameboardStr += elem.getValue();
                }

                if (r < gameboard[c].length - 1) {
                    gameboardStr += "\t | \t";
                }
            }
            if (c < gameboard[c].length - 1) {
                gameboardStr += "\n";
            }
        }

        return gameboardStr;
    }

    /**
     * Determines if the board is full of tiles that have all been matched,
     * indicating the game is over.
     * 
     * Precondition: gameboard is populated with tiles
     * 
     * @return true if all tiles have been matched, false otherwse
     */
    public boolean allTilesMatch() {
        for (Tile[] row : gameboard) {
            for (Tile elem : row) {
                if (!elem.matched()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets the tile to show its value (like a playing card face up)
     * 
     * Preconditions:
     * gameboard is populated with tiles,
     * row values must be in the range of 0 to gameboard.length,
     * column values must be in the range of 0 to gameboard[0].length
     * 
     * @param row    the row value of Tile
     * @param column the column value of Tile
     */
    public void show(int row, int column) {
        gameboard[column][row].show();
    }

    public String get(int row, int column) {
        return gameboard[column][row].getValue();
    }

    public Tile getTile(int row, int column) {
        return gameboard[column][row];
    }

    /**
     * Checks if the Tiles in the two locations match.
     * 
     * If Tiles match, show Tiles in matched state and return a "matched" message
     * If Tiles do not match, re-hide Tiles (turn face down).
     * 
     * Preconditions:
     * gameboard is populated with Tiles,
     * row values must be in the range of 0 to gameboard.length,
     * column values must be in the range of 0 to gameboard[0].length
     *
     * @param row1 the row value of Tile 1
     * @param col1 the column value of Tile 1
     * @param row2 the row value of Tile 2
     * @param col2 the column vlue of Tile 2
     * @return a message indicating whether or not a match occured
     */
    public boolean checkForMatch(int row1, int col1, int row2, int col2) {
        if (gameboard[col1][row1].equals(gameboard[col2][row2])) {
            gameboard[col1][row1].foundMatch();
            gameboard[col2][row2].foundMatch();

            return true;
        }

        return false;
    }

    /**
     * Checks the provided values fall within the range of the gameboard's dimension
     * and that the tile has not been previously matched
     * 
     * @param rpw the row value of Tile
     * @param col the column value of Tile
     * @return true if row and col fall on the board and the row,col tile is
     *         unmatched, false otherwise
     */
    public boolean validateSelection(int row, int col) {

        return (0 <= row && row < gameboard[0].length) && (0 <= col && col < gameboard.length);
    }

    public void hideAll() {
        for (int c = 0; c < gameboard.length; c++) {
            for (int r = 0; r < gameboard[0].length; r++) {
                gameboard[c][r].hide();
            }
        }
    }

    public int getColumns() {
        return gameboard.length;
    }

    public int getRows() {
        return gameboard[0].length;
    }
}
