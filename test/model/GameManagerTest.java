package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
        StartInfo startInfo = new StartInfo(4, 3, BoardType.HEXAGON);
        gameManager.initGM(startInfo);
    }

    @Test
    void initGM(){
        assertEquals(BoardType.HEXAGON ,gameManager.getStartInfo().getBoardType());
        assertEquals(4, gameManager.getStartInfo().getPlayerCount());
        assertEquals(3, gameManager.getStartInfo().getPieceCount());
        assertEquals(0, gameManager.getYutResults().size());
        assertEquals(false, gameManager.getExtraTurn());
    }

    @Test
    void getAllPiecePos() {
        List<PositionDTO> result = gameManager.getAllPiecePos();

        assertEquals(12, result.size());

        for (PositionDTO dto: result) {
            System.out.print(dto.toString());
        }

    }

    @Test
    void getYutResults() {
        gameManager.throwFixedYut("백도");
        gameManager.throwFixedYut("백도");
        gameManager.throwFixedYut("백도");
        gameManager.throwYut();
        gameManager.throwYut();
        gameManager.throwYut();

        ArrayList<YutResult> result = gameManager.getYutResults();
        assertEquals(6, result.size());
        assertEquals(YutResult.백도, result.get(0));
        assertEquals(YutResult.백도, result.get(1));
        assertEquals(YutResult.백도, result.get(2));
    }

    @Test
    void getExtraTurn() {
        gameManager.throwFixedYut("모");
        assertEquals(true, gameManager.getExtraTurn());
    }

    @Test
    void removeYutResult(){
        gameManager.getYutResults().clear();
        gameManager.throwFixedYut("도");
        gameManager.throwFixedYut("개");
        gameManager.throwFixedYut("걸");
        gameManager.throwFixedYut("윷");
        gameManager.throwFixedYut("모");

        gameManager.removeYutResult(3);

        assertFalse(gameManager.getYutResults().contains(YutResult.걸));
    }

    @Test
    void processYutResult(){
        int turn = gameManager.checkPlayer();

        gameManager.throwFixedYut("모");
        gameManager.throwFixedYut("도");
        gameManager.setSelectedpiece(1);
        gameManager.processYutResult(5);
        gameManager.processYutResult(1);

        assertEquals(50, gameManager.getAllPlayer().get(turn-1).getPieces(1).getStartCell().getId());

    }

    @Test
    void checkPlayer(){
        // 현재 플레이어 확인
        int turn = gameManager.checkPlayer();
        System.out.println(turn);
        gameManager.getYutResults().clear();
        gameManager.throwFixedYut("도");

        // 턴 아직 안 넘어간 상태
        assertEquals(turn, gameManager.checkPlayer());

        gameManager.removeYutResult(1);  // 이동 역할

        // 턴 넘김 확인
        assertEquals(turn+1, gameManager.checkPlayer());
    }

    @Test
    void checkWinner() {
        // 임시 테스트 - 2번 플레이어 승리하게
        for (Piece piece : gameManager.getAllPlayer().get(1).getALlPieces()){
            piece.setFinished(true);
        }

        int winnerIndex = gameManager.checkWin();

        assertEquals(2,winnerIndex);
    }

    @Test
    void findMovableCells(){
        // 윷 세팅
        gameManager.getYutResults().clear();
        gameManager.throwFixedYut("개");
        gameManager.throwFixedYut("도");
        gameManager.throwFixedYut("도");
        gameManager.throwFixedYut("걸");
        gameManager.throwFixedYut("윷");
        gameManager.throwFixedYut("모");

/*
        // 윷 결과 확인
        System.out.println(gameManager.getYutResults());

        // 이동 가능 지점 확인
        Map<Integer, Integer> movableMap = gameManager.findMovableCells(5);
        System.out.println(movableMap);
*/


        // 특정 cell에서 이동 가능한 셀 계산
        int startCellId = 1;
        Map<Integer, Integer> result = gameManager.findMovableCells(startCellId);

        // 검증({2=1, 3=2, 4=3, 5=4, 6=5})
        assertEquals(5, result.size());
        Map<Integer, Integer> expected = Map.of(
                2, 1,
                3, 2,
                4, 3,
                5, 4,
                6, 5
        );

        assertEquals(expected, result);


    }
}