package controller;

import model.*;
import org.junit.jupiter.api.Test;
import view.GameConfigView;
import view.GamePlayView;
import view.SwingConfigScreen;
import view.SwingPlayScreen;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        gameScreenController.FixedYutThrow("걸");
        List<YutResult> list1 =  gameScreenController.getYutResults();
        System.out.println(list1);

        // 4~7번 과정
        Map<Integer, Integer> list2 = gameScreenController.PieceSelect(1);
        System.out.println(list2);


        Integer key1 = list2.keySet().iterator().next();
        System.out.println(key1);
        gameScreenController.selectCoordinate(key1);

        List<PositionDTO> pieces = gameScreenController.takeOutPiece();
        for (PositionDTO piece : pieces) {
            System.out.println(piece);
        }


    }
}
