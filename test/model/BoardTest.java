package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player(1,4);
        player2 = new Player(2,4);
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
        //출발점 이동
        int move = 5;
        Piece piece = player1.getPieces(1);
        board.movePiecePositive(piece, move);

        Cell cell = board.getCell(move);
        assertEquals(cell.getId(), piece.getStartCell().getId());


    }

    @Test
    void movePiecePositive2() {
        //잡기
        int move = 5;
        Piece piece1 = player1.getPieces(1);
        Piece piece2 = player2.getPieces(1);

        board.movePiecePositive(piece1, move);
        boolean test = board.movePiecePositive(piece2, move);
        boolean expected = true;

        assertEquals(expected, test);
    }

    @Test
    void movePiecePositive3() {
        //완주
        int move = 31;
        Piece piece1 = player1.getPieces(1);

        board.movePiecePositive(piece1, move);

        boolean test = piece1.getFinished();
        boolean expected = true;

        assertEquals(expected, test);
    }

    @Test
    void movePiecePositive4() {
        //업기
        int move = 4;
        Piece piece1 = player1.getPieces(1);
        Piece piece2 = player1.getPieces(2);

        board.movePiecePositive(piece1, move);
        board.movePiecePositive(piece2, move);

        List<Piece> pieces = board.getCell(move).getPieces();
        int[] test = new int[2];
        int i = 0;
        for(Piece p : pieces) {
            test[i++] = p.getId();
        }

        int[] expected = { 1,2 };

        assertArrayEquals(expected, test);
    }

    @Test
    void movePieceNegative() {
        //단순 이동
        int move = 5;
        Piece piece = player1.getPieces(1);
        board.movePiecePositive(piece, move);

        board.movePieceNegative(piece);

        Cell cell = board.getCell(move-1);
        assertEquals(cell.getId(), piece.getStartCell().getId());
    }

    @Test
    void movePieceNegative2() {
        //잡기
        int move = 4;
        Piece piece1 = player1.getPieces(1);
        Piece piece2 = player2.getPieces(1);

        board.movePiecePositive(piece1, move);
        board.movePiecePositive(piece2, move+1);

        boolean test = board.movePieceNegative(piece2);
        boolean expected = true;

        assertEquals(expected, test);
    }

    @Test
    void movePieceNegative3() {
        //업기
        int move = 4;
        Piece piece1 = player1.getPieces(1);
        Piece piece2 = player1.getPieces(2);

        board.movePiecePositive(piece1, move);
        board.movePiecePositive(piece2, move+1);

        board.movePieceNegative(piece2);

        List<Piece> pieces = board.getCell(move).getPieces();
        int[] test = new int[2];
        int i = 0;
        for(Piece p : pieces) {
            test[i++] = p.getId();
        }

        int[] expected = { 1,2 };

        assertArrayEquals(expected, test);
    }
}