package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void getAllPiecePos() {
        List<PositionDTO> result = gameManager.getAllPiecePos();

        System.out.println("말 위치 정보 개수: "+result.size());

        for (PositionDTO dto: result) {
            System.out.println(dto.toString());
        }

        //TODO: 처음 상태 세팅 확인
        //assertEquals(12, result.size());


        //TODO: 말 하나 이동 시키고 상태 확인
    }

    @Test
    void getYutResults() {
        gameManager.throwFixedYut("백도");
        gameManager.throwFixedYut("백도");
        gameManager.throwFixedYut("백도");
        gameManager.throwYut();
        gameManager.throwYut();
        gameManager.throwYut();

        System.out.println(gameManager.getYutResults());
    }

    @Test
    void getExtraTurn() {
        System.out.println(gameManager.getExtraTurn());
    }

    @Test
    void checkWinner() {
        // 임시 테스트 - 2번 플레이어 승리하게
        for (Piece piece : gameManager.players.get(1).getALlPieces()){
            piece.setFinished(true);
        }

        int winnerIndex = gameManager.checkWin();
        if(winnerIndex==0){
            System.out.println("승자 없음. 게임 진행");
        }
        else{
            System.out.println(winnerIndex+"번 플레이어 승!");
        }
    }
}