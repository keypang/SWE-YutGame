package model;

public enum YutResult {
    백도(-1, false),
    도(1, false),
    개(2, false),
    걸(3, false),
    윷(4, true),
    모(5, true);

    private final int move;           // 이동할 칸 수
    private final boolean canRollAgain;  // 한 번 더 굴릴 수 있는지

    YutResult(int move, boolean canRollAgain) {
        this.move = move;
        this.canRollAgain = canRollAgain;
    }

    public int getMove() {
        return move;
    }

    public boolean canRollAgain() {
        return canRollAgain;
    }



}
