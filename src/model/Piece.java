package model;

public class Piece {
    private int id;
    private Cell startCell;
    private Cell priorCell;
    private boolean finished;
    private Player player;

    public Piece(int id, boolean finished, Player player) {
        this.id = id;
        this.finished = finished;
        this.player = player;

        this.priorCell = new Cell(-1,"임시", -1);
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setPriorCell(Cell priorCell) { this.priorCell = priorCell; }

    public Cell getPriorCell() { return priorCell; }

    public void clearPriorCell() {
        this.priorCell = null;
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

    public Player getPlayer() {
        return player;
    }
}
