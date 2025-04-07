package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YutThrowTest1 {

    @Test
    void yutthrow(){
        GameManager gameManager = new GameManager();
        Yut yut = new Yut();
        gameManager.setYut(yut);
        int result = gameManager.throwYut();
        System.out.println("랜덤 윷 결과: " + result);


        assertTrue(result >= 0 && result <= 4, "결과 값은 0 이상 4 이하이어야 합니다.");
    }
}
