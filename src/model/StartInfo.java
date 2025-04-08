package model;

public class StartInfo {

    private int playerCount;
    private int tokenCount;
    private BoardType boardType;

    public StartInfo(int playerCount, int tokenCount, BoardType boardType) {
        this.playerCount = playerCount;
        this.tokenCount = tokenCount;
        this.boardType = boardType;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }
}
