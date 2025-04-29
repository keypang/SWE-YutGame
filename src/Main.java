import controller.GameScreenController;
import controller.StartScreenController;
import model.Board;
import model.BoardType;
import model.GameManager;
import model.Player;
import view.GameConfigView;
import view.SwingConfigScreen;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        GameConfigView view = new SwingConfigScreen();
        StartScreenController startController = new StartScreenController(view, gameManager);

        // GameScreenController를 StartScreenController에서 접근할 수 있도록 제공
        startController.setGameManger(gameManager);
    }
}