package model;

public enum BoardType {

    SQUARE("사각형"),
    PENTAGON("오각형"),
    HEXAGON("육각형");

    private final String displayName;

    BoardType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}