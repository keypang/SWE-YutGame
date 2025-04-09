package model;

import java.util.ArrayList;

public class GameManager {
    private StartInfo startInfo;
    private Board board;
    private Yut yut;
    private int currentplayer = 1;
    private boolean extraTurn;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<YutResult> yutresults = new ArrayList<>();

    // 기본 생성자
    public GameManager() {

    }

    public void setStartInfo(StartInfo startInfo) {
        this.startInfo = startInfo;

        for(int i=1; i<=startInfo.getPlayerCount(); i++){
            players.add(new Player(i, startInfo.getPieceCount()));
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
    public boolean validate(int playerCount, int pieceCount, BoardType boardType) {

        if (playerCount < 2 || playerCount > 4) {
            return false;
        }
        if (pieceCount < 2 || pieceCount > 5) {
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
        System.out.println("Pieces: " + startInfo.getPieceCount());
        System.out.println("Board: " + startInfo.getBoardType());
    }

    public void checkPlayer() {
        // 리스트에 값이 들어있는지 확인
        // 있으면 currentplayer값 변경 x
        // 없으면 currentplayer값 1 증가시키는데 4를 넘어가는 값이면 다시 1로 변환
    }

    public void checkExtraTurn() {
        // extraTurn 변수가 true이면 더굴릴 수 있게
    }

    // 말이 잡혔는지 판단
    // 윷 결과가 남아있는지 판단

    // 랜덤 윷 던지기
    public YutResult throwYut() {
        YutResult result = yut.yutThrowRandom();
        yutresults.add(result);
        return result;
    }

    // 지정 윷 던지기 (테스트용)
    public YutResult throwFixedYut(String getresult) {
        YutResult result = yut.yutThrowFixed(getresult);
        yutresults.add(result);
        return result;
    }

    public void processYutResult(){
        // 여기에 current플레이어 이용
        // 들어온 값을 찾아서 리스트에서 없애야지

    }
}
