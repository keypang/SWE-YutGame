package model;

public class Piece {
    private int id;
    private Cell startCell;
    private boolean finished;
    private Player player;

    public Piece(int id, boolean finished, Player player) {
        this.id = id;
        this.finished = finished;
        this.player = player;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean getFinished() { return finished; }

    public Cell getStartCell() {
        return startCell;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }
}
