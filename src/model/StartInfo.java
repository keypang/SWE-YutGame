package model;

public class StartInfo {

  private int playerCount;
  private int pieceCount;
  private BoardType boardType;

  public StartInfo(int playerCount, int pieceCount, BoardType boardType) {
    this.playerCount = playerCount;
    this.pieceCount = pieceCount;
    this.boardType = boardType;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public int getPieceCount() {
    return pieceCount;
  }

  public void setPieceCount(int pieceCount) {
    this.pieceCount = pieceCount;
  }

  public BoardType getBoardType() {
    return boardType;
  }

  public void setBoardType(BoardType boardType) {
    this.boardType = boardType;
  }
}
