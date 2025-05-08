package model;

import java.lang.reflect.Array;
import java.util.*;

public class GameManager {
    private StartInfo startInfo;
    private Board board;
    private Yut yut;
    private int currentPlayer = 0;
    private int selectedpiece = 1;
    private boolean extraTurn = false;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<YutResult> yutResults = new ArrayList<>();
    ArrayList<PositionDTO> posInfo = new ArrayList<>();

    // ('이동 가능 cell id', '해당 cell로 가기 위해 이동해야 하는 칸 수')를 map 구조로 저장
    Map<Integer, Integer> movableMap = new HashMap<>();

    // 기본 생성자
    public GameManager() {
        this.yut = new Yut();
    }

    public int getCurrentPlayer() { return currentPlayer; }

    public int getSelectedpiece() {
        return selectedpiece;
    }

    public void setSelectedpiece(int selectedpiece) {
        this.selectedpiece = selectedpiece;
    }

    public void setExtraTurn(boolean extraTurn) {
        this.extraTurn = extraTurn;
    }

    public Boolean getExtraTurn(){
        return extraTurn;
    }

    public Map<Integer, Integer> getMovableMap() {
        return movableMap;
    }

    public Boolean isYutResultsEmpty() {
        return yutResults.isEmpty();
    }

    public void addYutResult(YutResult yutResult) {
        yutResults.add(yutResult);
    }

    public void removeYutResult(int target) {
        Iterator<YutResult> iterator = yutResults.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getMove() == target) {
                iterator.remove(); // 일치하는 첫 번째 항목 제거
                break;             // 더 이상 반복할 필요 없음
            }
        }
    }

    public void initGM(StartInfo startInfo) {
        // 기존 윷 결과 초기화
        yutResults.clear();
        extraTurn = false;

        // 플레이어 초기화
        setStartInfo(startInfo);

        // 플레이어 초기화
        initPlayers();

        // 윷놀이 판 설정
        this.board = new Board(players, startInfo.getBoardType());

        // 위치 정보 초기화
        initPosInfo();

        // 윷 초기화
        this.yut = new Yut();

        // 현재 플레이어 초기화
        currentPlayer = 1;

        // 디버깅용
        System.out.println("플레이어 수: " + startInfo.getPlayerCount() +
                ", 말 개수: " + startInfo.getPieceCount() +
                ", 보드 타입: " + startInfo.getBoardType());

        // 디버깅 용
        System.out.println("윷 결과 개수: " + yutResults.size() + ", 추가 턴 여부: " + extraTurn);
    }

    // 현재 선택되어 있는 말이 있는 셀 위치 정보
    public int getselectedsellid(){
        return players.get(currentPlayer-1).getPieces(selectedpiece).getStartCell().getId();
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

    // 턴 넘기는 메소드
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

    public void processYutResult(int move){
        System.out.println("현재 선택된 플레이어:"+currentPlayer+"// 현재 선택된 말:"+selectedpiece);
        if (move == -1){
            boolean checkextra = board.movePieceNegative(players.get(currentPlayer-1).getPieces(selectedpiece));
            if (checkextra) { extraTurn = true; }
            else { extraTurn = false;}
        }
        else {
            boolean checkextra = board.movePiecePositive(players.get(currentPlayer-1).getPieces(selectedpiece), move);
            if (checkextra) { extraTurn = true; }
            else { extraTurn = false;}
        }
        System.out.println(getAllPiecePos());
        System.out.println("말 옮기고 나서 선택된 플레이어:"+currentPlayer+"// 현재 선택된 말:"+selectedpiece);
    }

    // 전체 말 위치 세팅
    public void setPosInfo(){
        // player 돌면서 각 player의 말 정보 받아오기
        for (Player player : players) {
            int playerId = player.getId();
            Piece[] pieces = player.getALlPieces();

            for (Piece piece : pieces) {
                // 말 id랑 현재 위치 cell id 받아오기
                int pieceId = piece.getId();
                int cellId;

                // 완주한 말은 21번 셀(완주 셀)로 설정
                if (piece.isFinished()) {
                    if (startInfo.getBoardType() == BoardType.SQUARE) cellId = 21; // 완주 셀 ID 지정
                    else if (startInfo.getBoardType() == BoardType.PENTAGON) cellId = 26;
                    else cellId = 31;

                } else {
                    cellId = piece.getStartCell().getId();
                }

                // 플레이어 id와 말 id 일치하는 DTO 객체 받아와서 데이터 담기
                for(PositionDTO positionDTO : posInfo) {
                    PositionDTO dto = positionDTO.getCorrectDTO(pieceId, playerId);
                    if (dto != null) {
                        dto.setCellId(cellId);
                    }
                }
            }
        }
    }

    // 전체 말 위치 리턴
    public ArrayList<PositionDTO> getAllPiecePos() {
        // 위치 정보 업데이트
        setPosInfo();

        // 전달
        return posInfo;
    }

    // 윷 결과 리스트 반환
    public ArrayList<YutResult> getYutResults() {
        return yutResults;
    }

    // Cell id 넘겨받아서 이동 가능한 Cell 리스트 반환
    public Map<Integer, Integer> findMovableCells(int cellId){

        // moveableMap 초기화
        movableMap.clear();

        // 현재 위치한 Cell
        Cell currentCell =  board.getCell(cellId);

        // 보유하고 있는 윳 리스트
        ArrayList<Integer> movableNumList = new ArrayList<>();
        for(YutResult yut: yutResults){
            // 이동하는 칸 수로 변환
            movableNumList.add(yut.getMove());
        }

        // 중복 제거하고 int 배열로 변환
        int[] movableNumArray = movableNumList.stream().distinct().mapToInt(Integer::intValue).toArray();
        int[] movableCellIdArray = new int[movableNumArray.length];

        movableCellIdArray = board.getMovableCells(currentCell, movableNumArray); // 이동 가능 cell id list


        for(int i=0; i<movableCellIdArray.length; i++){
            movableMap.put(movableCellIdArray[i], movableNumArray[i]);
        }

        // 중간 과정 디버깅용 출력
        System.out.println("윷 리스트: " + yutResults.toString());
        System.out.println("이동 가능 칸 수: " + movableNumList);
        System.out.println("이동 가능 칸 수(중복 제거): " + Arrays.toString(movableNumArray));
        System.out.println("이동 가능 지점 리스트: " + Arrays.toString(movableCellIdArray));

        return movableMap;
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
