package model;

import java.util.ArrayList;

public class GameManager {
    private StartInfo startInfo;
    private Board board;
    private Yut yut;
    private int currentplayer = 1;
    private boolean extraTurn = false;
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
        System.out.println("Tokens: " + startInfo.getPieceCount());
        System.out.println("Board: " + startInfo.getBoardType());
    }

    public void checkPlayer() {
        // 리스트에 값이 들어있는지 확인 + 추가 턴 여부 확인
        // 있으면 currentplayer값 변경 x
        // 없으면 currentplayer값 1 증가시키는데 4를 넘어가는 값이면 다시 1로 변환
        if(yutresults.isEmpty() && extraTurn==false) {
            currentplayer++;
            if (currentplayer > 4) {
                currentplayer = 1;
            }
        }
    }

    // 윷 결과가 남아있는지 판단

    // 랜덤 윷 던지기
    public YutResult throwYut() {
        YutResult result = yut.yutThrowRandom();
        yutresults.add(result);
        // 윷, 모 판단 후 추가 턴 여부 체크
        if (result.canRollAgain()) {
            extraTurn = true;
            System.out.println(extraTurn);
        }
        return result;
    }

    // 지정 윷 던지기 (테스트용)
    public YutResult throwFixedYut(String getresult) {
        YutResult result = yut.yutThrowFixed(getresult);
        yutresults.add(result);
        // 윷, 모 판단 후 추가 턴 여부 체크
        if (result.canRollAgain()) {
            extraTurn = true;
            System.out.println(extraTurn);
        }
        return result;
    }

    public void calculatePlayerResult(String selectedyut){

    }

    public void processYutResult(int piecenum, String getresult){
        // 여기에 current플레이어 이용
        // 들어온 값을 찾아서 리스트에서 없애야지
        for (YutResult result : yutresults) {
            if (result.name().equals(getresult)) {
                board.movePiecePostive(players.get(currentplayer).getPieces(piecenum), result.getMove());
                // 여기서 잡혔는지 안잡혔는지 판단해야함. 만약에 잡혔으면 extraTurn은 true로 바꿔줘야함.
                yutresults.remove(result);
            }
        }
    }
}
