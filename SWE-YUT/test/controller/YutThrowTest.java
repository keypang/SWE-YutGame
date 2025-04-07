package controller;

import model.GameManager;
import org.junit.jupiter.api.Test;
import view.GameView;
import view.SwingView;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YutThrowTest {

    @Test
    void testyutthrow(){
        GameManager gameManager = new GameManager();
        GameView view = new SwingView();
        GameScreenController gameScreenController = new GameScreenController(view, gameManager);

        int result = gameScreenController.RandomYutThrow();
        System.out.println("랜덤 윷 결과: " + result);

        assertTrue(result >= 0 && result <= 4, "결과 값은 0 이상 4 이하이어야 합니다.");
    }
}
