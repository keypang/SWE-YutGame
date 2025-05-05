package model;

public class Player {
    private int id;
    private Piece[] pieces;

    public Player(int id, int pieceNum) {
        this.id = id;
        this.pieces = new Piece[pieceNum];
        makePiece(pieceNum);
    }

    private void makePiece(int pieceNum) {
        for(int i = 0; i < pieceNum; i++) {
            pieces[i] = new Piece(i,false, this);
        }
    }

    public Piece getPieces(int id) {
        return pieces[id];
    }

    public Piece[] getALlPieces(){
        return pieces.clone();
    }

    public int getPieceNum() {
        return pieces.length;
    }

    public int getId() {
        return id;
    }
}