package Board;

public class Tile {
    private String value;
    private boolean matched = false;
    private boolean showing = false;
    private boolean faceUp = false;

    public Tile(String value) {
        this.value = value;
    }

    public void reset(String value) {
        this.value = value;
        matched = false;
        showing = false;
        faceUp = false;
    }

    public String getValue() {
        return value;
    }

    public void show() {
        showing = true;
    }

    public void hide() {
        showing = false;
    }

    public boolean isShowing() {
        return showing;
    }

    public void foundMatch() {
        matched = true;
    }

    public boolean matched() {
        return matched;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void flip(boolean showFace) {
        faceUp = showFace;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Tile) {
            Tile other = (Tile) obj;
            return this.value.equals(other.value);
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
