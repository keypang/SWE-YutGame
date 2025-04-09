package controller;

import model.GameManager;
import model.YutResult;
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

        YutResult result = gameScreenController.RandomYutThrow();
        System.out.println("랜덤 윷 결과: " + result);


    }
}
