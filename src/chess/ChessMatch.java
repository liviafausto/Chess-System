package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private final Board BOARD;
    private List<Piece> boardPieces = new ArrayList<>();
    private boolean check;

    public ChessMatch(){
        BOARD = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() { return turn; }

    public Color getCurrentPlayer() { return currentPlayer; }

    public boolean getCheck(){ return check; }

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

        if(testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check.");
        }

        check = testCheck(opponent(currentPlayer));
        nextTurn();
        return (ChessPiece)capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        Piece sourcePiece = BOARD.removePiece(source); // Removes the piece from the source position
        Piece capturedPiece = BOARD.removePiece(target); // Captures possible piece on target position
        BOARD.placePiece(sourcePiece, target);

        if(capturedPiece != null){
            boardPieces.remove(capturedPiece);
        }
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        Piece sourcePiece = BOARD.removePiece(target);
        BOARD.placePiece(sourcePiece, source);

        if(capturedPiece != null){
            BOARD.placePiece(capturedPiece, target);
            boardPieces.add(capturedPiece);
        }
    }

    private void validateSourcePosition(Position position){
        if(!BOARD.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position.");
        }
        if(currentPlayer != ((ChessPiece)BOARD.getPiece(position)).getColor()){
            throw new ChessException("The chosen piece is not yours.");
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
        currentPlayer = opponent(currentPlayer);
    }

    private Color opponent(Color color){
        if(color == Color.WHITE){
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    private ChessPiece getKing(Color kingsColor){
        List<Piece> playersPieces = boardPieces.stream().filter(x->((ChessPiece)x).getColor() == kingsColor).collect(Collectors.toList());
        for(Piece piece : playersPieces){
            if(piece instanceof King){
                return (ChessPiece)piece;
            }
        }
        throw new IllegalStateException("The is no " + kingsColor + " king on the board.");
    }

    private boolean testCheck(Color color){
        Position kingsPosition = getKing(color).getChessPosition().toPosition();
        List<Piece> opponentsPieces = boardPieces.stream().filter(x->((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());

        for(Piece piece : opponentsPieces){
            boolean[][] possibleMoves = piece.possibleMoves();
            if(possibleMoves[kingsPosition.getRow()][kingsPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private void placeNewPiece(char column, int row, ChessPiece chessPiece){
        BOARD.placePiece(chessPiece, new ChessPosition(column, row).toPosition());
        boardPieces.add(chessPiece);
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
