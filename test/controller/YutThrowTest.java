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


        Integer value = list2.values().iterator().next();
        System.out.println(value);
        gameScreenController.selectCoordinate(list2);

        List<PositionDTO> pieces = gameScreenController.takeOutPiece();
        for (PositionDTO piece : pieces) {
            System.out.println(piece);
        }


        /////////////////////
        // 다시 동일과정 진행 //
        gameScreenController.FixedYutThrow("걸");
        List<YutResult> list3 =  gameScreenController.getYutResults();
        System.out.println(list3);

        Map<Integer, Integer> list4 = gameScreenController.PieceSelect(2);
        System.out.println(list4);


        Integer value2 = list2.values().iterator().next();
        System.out.println(value2);
        gameScreenController.selectCoordinate(list4);

        List<PositionDTO> pieces2 = gameScreenController.takeOutPiece();
        for (PositionDTO piece : pieces2) {
            System.out.println(piece);
        }

        /////////////////////
        // 다시 동일과정 진행 //
        gameScreenController.FixedYutThrow("걸");
        List<YutResult> list5 =  gameScreenController.getYutResults();
        System.out.println(list5);

        Map<Integer, Integer> list6 = gameScreenController.PieceSelect(3);
        System.out.println(list6);


        Integer value3 = list6.values().iterator().next();
        System.out.println(value3);
        gameScreenController.selectCoordinate(list6);

        List<PositionDTO> pieces3 = gameScreenController.takeOutPiece();
        for (PositionDTO piece : pieces3) {
            System.out.println(piece);
        }

    }
}
