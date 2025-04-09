import controller.StartScreenController;
import model.*;
import view.GameView;
import view.SwingView;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player(1,4);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        Board board = new Board(players,BoardType.HEXAGON);

        Scanner in = new Scanner(System.in);
        System.out.println("숫자 입력");
        int test = in.nextInt();
        while(test > -100){
            if(test > 0) {
                board.movePiecePositive(player1.getPieces(0),test);
            }
            else {
                board.movePieceNegative(player1.getPieces(0));
            }

            for(int i = 0; i < 31; i++){
                Cell testCell = board.getCell(i);
                if(!testCell.getPieces().isEmpty()){
                    System.out.println("1");
                    System.out.println(testCell.getPieces().getFirst().getId());
                }
            }

            System.out.println("숫자 입력");
            test = in.nextInt();
        }
/*        GameManager gameManager = new GameManager();
        GameView view = new SwingView();
        StartScreenController controller = new StartScreenController(view, gameManager);*/

    }
}