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

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("Player 숫자 입력");
            int playerNum = scanner.nextInt();
            System.out.println("Piece ID 입력");
            int pieceNum = scanner.nextInt();
            System.out.println("윷 입력");
            int yutNum = scanner.nextInt();
            if(playerNum == 1){
                board.movePiecePositive(player1.getPieces(pieceNum),yutNum);
            }
            else if(playerNum == 2){
                board.movePiecePositive(player2.getPieces(pieceNum),yutNum);
            }
            else{
                break;
            }
        }
    }
}