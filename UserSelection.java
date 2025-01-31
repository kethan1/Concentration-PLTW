public class UserSelection {
    int row;
    int column;
    boolean valid;

    public UserSelection(int row, int column) {
        this.row = row;
        this.column = column;
        this.valid = true;
    }

    public UserSelection() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
