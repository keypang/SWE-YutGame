package model;

public class StartInfo {

    private int playerCount;
    private int pieceCount;
    private BoardType boardType;

    public StartInfo(int playerCount, int tokenCount, BoardType boardType) {
        this.playerCount = playerCount;
        this.pieceCount = tokenCount;
        this.boardType = boardType;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getPieceCount() { return pieceCount; }

    public void setPieceCount(int tokenCount) {
        this.pieceCount = tokenCount;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
}
