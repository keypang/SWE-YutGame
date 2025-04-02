package model;

public class Model {

    private StartInfo startInfo;

    // 기본 생성자
    public Model() {
    }

    public void setStartInfo(StartInfo startInfo) {
        this.startInfo = startInfo;
    }

    public StartInfo getStartInfo() {
        return startInfo;
    }

    // 유효성 검사
    public boolean validate(int playerCount, int tokenCount, BoardType boardType) {

        if (playerCount < 2 || playerCount > 4) {
            return false;
        }
        if (tokenCount < 2 || tokenCount > 5) {
            return false;
        }
        if (boardType == null) {
            return false;
        }
        return true;
    }

    // 모델에 제대로 startInfo 정보가 전달되었는지 확인하기 위한 메소드
    public void test() {

        System.out.println("Game started with settings:");
        System.out.println("Players: " + startInfo.getPlayerCount());
        System.out.println("Tokens: " + startInfo.getTokenCount());
        System.out.println("Board: " + startInfo.getBoardType());
    }
}