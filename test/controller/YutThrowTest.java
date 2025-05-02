package controller;

import model.BoardType;
import model.GameManager;
import model.StartInfo;
import model.YutResult;
import org.junit.jupiter.api.Test;
import view.GameConfigView;
import view.GamePlayView;
import view.SwingConfigScreen;
import view.SwingPlayScreen;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YutThrowTest {

    @Test
    void testyutthrow(){
        GamePlayView view = new SwingPlayScreen(4,4, BoardType.SQUARE);
        GameManager gameManager = new GameManager();
        StartInfo startInfo = new StartInfo(4,4, BoardType.SQUARE);
        gameManager.initGM(startInfo);
        GameScreenController gameScreenController = new GameScreenController(view, gameManager);


        // 2~3번 과정
        gameScreenController.RandomYutThrow();
        List<YutResult> list1 =  gameScreenController.getYutResults();
        System.out.println(list1);

        int[] list2 = gameScreenController.PieceSelect(1);
        System.out.println(Arrays.toString(list2));

        // gameScreenController.selectCoordinate(list2[0]);

        // 이러고 전체 상태한번 출력해보면 되는거 같은데

    }
}
