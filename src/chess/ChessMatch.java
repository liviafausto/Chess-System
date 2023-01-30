package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board BOARD;

    public ChessMatch(){
        BOARD = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() { return turn; }

    public Color getCurrentPlayer() { return currentPlayer; }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] chessPieces = new ChessPiece[BOARD.getRows()][BOARD.getColumns()];

        for(int i=0; i<BOARD.getRows(); i++){
            for(int j=0; j<BOARD.getColumns(); j++){
                chessPieces[i][j] = (ChessPiece)BOARD.getPiece(i, j);
            }
        }
        return chessPieces;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position source = sourcePosition.toPosition();
        validateSourcePosition(source);
        return BOARD.getPiece(source).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        return (ChessPiece)capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        Piece sourcePiece = BOARD.removePiece(source); // Removes the piece from the source position
        Piece capturedPiece = BOARD.removePiece(target); // Captures possible piece on target position
        BOARD.placePiece(sourcePiece, target);
        return capturedPiece;
    }

    private void validateSourcePosition(Position position){
        if(currentPlayer != ((ChessPiece)BOARD.getPiece(position)).getColor()){
            throw new ChessException("The chosen piece is not yours.");
        }
        if(!BOARD.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position.");
        }
        if(!BOARD.getPiece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!BOARD.getPiece(source).possibleMove(target)){
            throw new ChessException("The chosen piece can't move to target position.");
        }
    }

    private void nextTurn(){
        turn++;
        if(currentPlayer == Color.WHITE){
            currentPlayer = Color.BLACK;
        } else {
            currentPlayer = Color.WHITE;
        }
    }

    private void placeNewPiece(char column, int row, ChessPiece chessPiece){
        BOARD.placePiece(chessPiece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup(){
        placeNewPiece('a', 8, new Rook(BOARD, Color.BLACK));
        placeNewPiece('b', 8, new Knight(BOARD, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(BOARD, Color.BLACK));
        placeNewPiece('d', 8, new Queen(BOARD, Color.BLACK));
        placeNewPiece('e', 8, new King(BOARD, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(BOARD, Color.BLACK));
        placeNewPiece('g', 8, new Knight(BOARD, Color.BLACK));
        placeNewPiece('h', 8, new Rook(BOARD, Color.BLACK));

        placeNewPiece('a', 1, new Rook(BOARD, Color.WHITE));
        placeNewPiece('b', 1, new Knight(BOARD, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(BOARD, Color.WHITE));
        placeNewPiece('d', 1, new Queen(BOARD, Color.WHITE));
        placeNewPiece('e', 1, new King(BOARD, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(BOARD, Color.WHITE));
        placeNewPiece('g', 1, new Knight(BOARD, Color.WHITE));
        placeNewPiece('h', 1, new Rook(BOARD, Color.WHITE));
    }

}
