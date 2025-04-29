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
        /*GameManager gameManager = new GameManager();
        GameConfigView view = new SwingConfigScreen();
        StartScreenController startController = new StartScreenController(view, gameManager);

        // GameScreenController를 StartScreenController에서 접근할 수 있도록 제공
        startController.setGameManger(gameManager);*/

        //테스트 코드 siyoon

        Player player1 = new Player(1,4);
        Player player2 = new Player(2,4);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        Board board = new Board(players, BoardType.SQUARE);
        int[] moves = new int[3];
        moves[0] = 1;
        moves[1] = 4;
        moves[2] = 5;
        int[] movea = board.getMovableCells(board.getCell(5),moves);

        for (int j : movea) {
            System.out.println(j);
        }
    }
}