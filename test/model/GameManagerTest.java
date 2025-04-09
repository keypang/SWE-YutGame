package model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @Test
    void getAllPiecePos() {
        GameManager gameManager = new GameManager();
        StartInfo startInfo = new StartInfo(4, 3, BoardType.HEXAGON);
        gameManager.setStartInfo(startInfo);

        List<Map.Entry<Integer, Integer>> result = gameManager.getAllPiecePos();

        System.out.println("말 위치 정보 개수: "+result.size());

        for (Map.Entry<Integer, Integer> entry : result) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        //TODO: 처음 상태 세팅 확인
        //assertEquals(12, result.size());


        //TODO: 말 하나 이동 시키고 상태 확인


    }
}