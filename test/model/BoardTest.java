package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        /*gameManager = new GameManager();
        StartInfo startInfo = new StartInfo(4, 3, BoardType.HEXAGON);
        gameManager.initGM(startInfo);*/
        Player player1 = new Player(1,4);
        Player player2 = new Player(2,4);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        board = new Board(players, BoardType.HEXAGON);
    }
    @Test
    void getMovableCells() {
        int[] movableCells = new int[4];
        movableCells[0] = 1;
        movableCells[1] = 2;
        movableCells[2] = 3;
        movableCells[3] = -1;

        Cell cell = board.getCell(5);

        int[] result = board.getMovableCells(cell, movableCells);

        int[] expected = { 50, 55, 1000, 4 };

        //assertArrayEquals(expected, result);
        for(int i: result) {
            System.out.print(i + " ");
        }
    }

    @Test
    void movePiecePositive() {
    }

    @Test
    void movePieceNegative() {
    }
}