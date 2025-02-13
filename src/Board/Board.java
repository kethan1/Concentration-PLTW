package Board;

import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final String[] TILE_VALUES = {
        "lion", "lion",
        "penguin", "penguin",
        "dolphin", "dolphin",
        "fox", "fox",
        "monkey", "monkey",
        "turtle", "turtle"
    };

    private Tile[][] tiles;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private int lastRow1, lastCol1, lastRow2, lastCol2;

    public enum Outcome {
        WAITING, MATCH, NO_MATCH
    }

    public static class MoveResult {
        public Outcome outcome;
        public int firstRow, firstCol;
        public int secondRow, secondCol;

        public MoveResult(Outcome outcome, int firstRow, int firstCol, int secondRow, int secondCol) {
            this.outcome = outcome;
            this.firstRow = firstRow;
            this.firstCol = firstCol;
            this.secondRow = secondRow;
            this.secondCol = secondCol;
        }
    }

    public Board() {
        int rows = 3;
        int cols = 4;
        if (rows * cols != TILE_VALUES.length || TILE_VALUES.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid board dimensions or tile values.");
        }
        tiles = new Tile[rows][cols];

        List<String> values = new ArrayList<>(Arrays.asList(TILE_VALUES));
        Collections.shuffle(values);
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tiles[row][col] = new Tile(values.get(index++));
            }
        }
    }

    public int getRows() {
        return tiles.length;
    }

    public int getColumns() {
        return tiles[0].length;
    }

    public String getTileValue(int row, int col) {
        return tiles[row][col].getValue();
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public boolean allTilesMatched() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (!tile.matched()) {
                    return false;
                }
            }
        }
        return true;
    }

    public MoveResult selectTile(int row, int col) {
        if (!validateSelection(row, col)) {
            throw new IllegalArgumentException("Invalid tile selection");
        }
        Tile tile = tiles[row][col];
        if (tile.isShowing() || tile.matched()) {
            return null;
        }
        tile.show();
        if (selectedRow == -1) {
            selectedRow = row;
            selectedCol = col;
            return new MoveResult(Outcome.WAITING, row, col, -1, -1);
        } else {
            lastRow1 = selectedRow;
            lastCol1 = selectedCol;
            lastRow2 = row;
            lastCol2 = col;

            MoveResult result;
            if (tiles[selectedRow][selectedCol].equals(tile)) {
                tiles[selectedRow][selectedCol].foundMatch();
                tile.foundMatch();
                result = new MoveResult(Outcome.MATCH, selectedRow, selectedCol, row, col);
            } else {
                result = new MoveResult(Outcome.NO_MATCH, selectedRow, selectedCol, row, col);
            }

            selectedRow = -1;
            selectedCol = -1;

            return result;
        }
    }

    public void resetLastMove() {
        if (!tiles[lastRow1][lastCol1].matched()) {
            tiles[lastRow1][lastCol1].hide();
        }
        if (!tiles[lastRow2][lastCol2].matched()) {
            tiles[lastRow2][lastCol2].hide();
        }
    }

    public boolean validateSelection(int row, int col) {
        return row >= 0 && row < getRows() && col >= 0 && col < getColumns();
    }
}
