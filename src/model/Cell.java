package model;

import java.util.ArrayList;
import java.util.List;

public class Cell {

  private int id;
  private String type; // 출발, 도착, 일반길, 갈림길, 지름길, 교차로
  private int lineNum; //지름길 번호
  private List<Cell> nextCells = new ArrayList<>();
  private List<Cell> previousCells = new ArrayList<>();
  private List<Piece> pieces = new ArrayList<>();

  public Cell(int id, String type, int lineNum) {
    this.id = id;
    this.type = type;
    this.lineNum = lineNum;
  }

  public void addNextCell(Cell nextCell) {
    this.nextCells.add(nextCell);
  }

  public void addPreviousCell(Cell previousCell) {
    this.previousCells.add(previousCell);
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public List<Cell> getNextCells() {
    return nextCells;
  }

  public List<Cell> getPreviousCells() {
    return previousCells;
  }

  public int getId() {
    return id;
  }

  public List<Piece> getPieces() {
    return pieces;
  }

  public void addPiece(Piece piece) {
    this.pieces.add(piece);

  }

  public void clearPieces() {
    this.pieces.clear();
  }

  public Cell getPreviousCellWithMinId() {
    if (previousCells.isEmpty()) {
      return null;
    }

    Cell minCell = previousCells.getFirst();
    for (Cell cell : previousCells) {
      if (cell.getId() < minCell.getId()) {
        minCell = cell;
      }
    }
    return minCell;
  }

  public boolean checkCatchPiece(Piece piece) {
    if (pieces.isEmpty()) {
      return false;
    } else {
      return !pieces.getFirst().getPlayer().equals(piece.getPlayer());
    }
  }

  public int getLineNum() {
    return lineNum;
  }
}