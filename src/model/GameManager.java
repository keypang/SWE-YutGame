package model;

import java.util.*;

public class GameManager {
    private StartInfo startInfo;
    private Board board;
    private Yut yut;
    private int currentPlayer = 1;
    private boolean extraTurn = false;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<YutResult> yutResults = new ArrayList<>();
    ArrayList<PositionDTO> posInfo = new ArrayList<>();

    // 기본 생성자
    public GameManager() {
        this.yut = new Yut();
    }

    public void initGM(StartInfo startInfo) {
        setStartInfo(startInfo);

        initPlayers();

        // 윷놀이 판 설정
        this.board = new Board(players, startInfo.getBoardType());

        initPosInfo();

        // 윷 초기화
        this.yut = new Yut();

        System.out.println("게임 모델 생성 끝!");
    }

    // 사용자 입력 정보 저장(윷놀이 판 생성)
    public void setStartInfo(StartInfo startInfo) {
        this.startInfo = startInfo;
    }

    // 플레이어 초기화
    public void initPlayers(){
        for(int i=1; i<=startInfo.getPlayerCount(); i++){
            players.add(new Player(i, startInfo.getPieceCount()));
        }
    }

    // 최초 위치 정보 세팅
    public void initPosInfo(){
        // 말 id, 플레이어 id, 지점 id
        for (Player player : players) {
            int playerId = player.getId();
            Piece[] pieces = player.getALlPieces();

            for (Piece piece : pieces) {
                // 말 id 받아오기
                int pieceId =  piece.getId();

                // DTO 객체 생성
                posInfo.add(new PositionDTO(pieceId, playerId));
            }
        }
    }

    // 초기 설정값 리턴
    public StartInfo getStartInfo() {
        return startInfo;
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

    public int checkPlayer() {
        // 윷 리스트에 값이 들어있는지 + 추가 턴 여부 확인
        // 있으면 currentplayer값 변경 x
        // 없으면 currentplayer값 1 증가시키는데 4를 넘어가는 값이면 다시 1로 변환
        if(yutResults.isEmpty() && extraTurn == false) {
            currentPlayer++;
            if(currentPlayer > startInfo.getPlayerCount()) {
                currentPlayer = 1;  // 전체 수 넘어가면 다시 1번으로
            }
        }

        return currentPlayer;
    }

    // 윷 결과가 남아있는지 판단

    // 랜덤 윷 던지기
    public YutResult throwYut() {
        YutResult result = yut.yutThrowRandom();
        yutResults.add(result);
        // 윷, 모 판단 후 추가 턴 여부 체크
        if (result.canRollAgain()) {
            extraTurn = true;
            // System.out.println(extraTurn);
        }
        return result;
    }

    // 지정 윷 던지기 (테스트용)
    public YutResult throwFixedYut(String getResult) {
        YutResult result = yut.yutThrowFixed(getResult);
        yutResults.add(result);
        // 윷, 모 판단 후 추가 턴 여부 체크
        if (result.canRollAgain()) {
            extraTurn = true;
            System.out.println(extraTurn);
        }
        return result;
    }

    public void calculatePlayerResult(String selectedYut){

    }

    public void processYutResult(int pieceNum, String getResult){
        // 여기에 current플레이어 이용
        // 들어온 값을 찾아서 리스트에서 없애야지
        for (YutResult result : yutResults) {
            if (result.name().equals(getResult)) {
                board.movePiecePostive(players.get(currentPlayer).getPieces(pieceNum), result.getMove());
                // 여기서 잡혔는지 안잡혔는지 판단해야함. 만약에 잡혔으면 extraTurn은 true로 바꿔줘야함.
                yutResults.remove(result);
            }
        }
    }

    // 전체 말 위치 세팅
    public void setPosInfo(){
        // player 돌면서 각 player의 말 정보 받아오기
        for (Player player : players) {
            int playerId = player.getId();
            Piece[] pieces = player.getALlPieces();

            for (Piece piece : pieces) {
                // 말 id랑 현재 위치 cell id 받아오기
                int pieceId =  piece.getId();
                int cellId = piece.getStartCell().getId();

                // 플레이어 id와 말 id 일치하는 DTO 객체 받아와서 데이터 담기
                for(PositionDTO positionDTO : posInfo) {
                    PositionDTO dto = positionDTO.getCorrectDTO(playerId, pieceId);
                    if (dto != null) {
                        dto.setCellId(cellId);
                    }
                }
            }
        }
    }

    // 전체 말 위치 리턴
    public List<PositionDTO> getAllPiecePos() {
        // 위치 정보 업데이트
        setPosInfo();

        // 전달
        return posInfo;
    }

    // 윷 결과 리스트 반환
    public ArrayList<YutResult> getYutResults() {
        return yutResults;
    }

    // 추가 기회 여부 반환
    public Boolean getExtraTurn(){
        return extraTurn;
    }

    // 승리 판단 - 0이면 계속 진행, 유효한 플레이어 인덱스면 게임 종료
    public int checkWin(){
        int winnerIndex = 0;
        for (Player player : players) {
            boolean allFinished = true;

            for (Piece piece : player.getALlPieces()) {
                if (!piece.isFinished()){
                    // 말이 남아있으면 다음 플레이어 판단으로 넘어감
                    allFinished = false;
                    break;
                }
            }

            if(allFinished){
                winnerIndex = player.getId();
            }
        }

        return winnerIndex;
    }
}
