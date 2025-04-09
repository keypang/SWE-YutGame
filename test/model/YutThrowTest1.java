package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YutThrowTest1 {

    @Test
    void yutthrow(){
        GameManager gameManager = new GameManager();
        Yut yut = new Yut();
        gameManager.setYut(yut);
        YutResult result1 = gameManager.throwYut();
        YutResult result2 = gameManager.throwFixedYut("도");
        System.out.println("랜덤 윷 결과: " + result1);
        System.out.println("고정 윷 결과: " + result2);


        assertTrue(
                result1.name().equals("도") ||
                        result1.name().equals("개") ||
                        result1.name().equals("걸") ||
                        result1.name().equals("윷") ||
                        result1.name().equals("모") ||
                        result1.name().equals("백도")
        );
    }
}
