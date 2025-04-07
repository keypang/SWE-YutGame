package model;

public class Piece {
    private int id;
    private Cell startCell;
    private Cell priorCell;
    private boolean finished;

    public Piece(int id, boolean finished){
        this.id = id;
        this.finished = finished;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getId() {
        return id;
    }
}
