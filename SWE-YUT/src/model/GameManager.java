package model;

import java.util.ArrayList;

public class GameManager {
    private StartInfo startInfo;
    private Board board;
    private Yut yut;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Integer> yutresults = new ArrayList<>();

    // 기본 생성자
    public GameManager() {

    }

    public void setStartInfo(StartInfo startInfo) {
        this.startInfo = startInfo;

        for(int i=1; i<=startInfo.getPlayerCount(); i++){
            players.add(new Player(i, startInfo.getTokenCount()));
        }
        board = new Board(players, startInfo.getBoardType());
        this.yut = new Yut();
        System.out.println("게임 모델 생성 끝!");
    }

    public StartInfo getStartInfo() {
        return startInfo;
    }

    public void setYut(Yut yut) {
        this.yut = yut;
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

    // 랜덤 윷 던지기
    public int throwYut() {
        int result = yut.yutThrowRandom();
        yutresults.add(result);
        return result;
    }

    // 지정 윷 던지기 (테스트용)
    public int throwFixedYut() {
        int result = yut.yutThrowFixed();
        yutresults.add(result);
        return result;
    }
}
