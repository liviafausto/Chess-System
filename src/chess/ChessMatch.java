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
    private boolean check;
    private boolean checkmate;
    private List<Piece> boardPieces = new ArrayList<>();

    public ChessMatch(){
        BOARD = new Board(8,8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public int getTurn() { return turn; }

    public Color getCurrentPlayer() { return currentPlayer; }

    public boolean getCheck(){ return check; }

    public boolean getCheckmate(){ return checkmate; }

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

        if(testCheckMate(opponent(currentPlayer))){
            checkmate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece)capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        // 1. Remove the piece from the source position and increase its move count
        ChessPiece sourcePiece = (ChessPiece) BOARD.removePiece(source);
        sourcePiece.increaseMoveCount();
        // 2. Remove a possible piece from target position
        Piece capturedPiece = BOARD.removePiece(target);
        // 3. Place source piece on target position
        BOARD.placePiece(sourcePiece, target);
        // 4. If there was a piece captured from target position, remove it from the board list
        if(capturedPiece != null){
            boardPieces.remove(capturedPiece);
        }
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece){
        // 1. Remove source piece from target position and decrease its move count
        ChessPiece sourcePiece = (ChessPiece) BOARD.removePiece(target);
        sourcePiece.decreaseMoveCount();
        // 2. Place source piece back on source position
        BOARD.placePiece(sourcePiece, source);
        // 3. If there was a piece captured when the move was made
        if(capturedPiece != null){
            BOARD.placePiece(capturedPiece, target); // Place it back on target position
            boardPieces.add(capturedPiece); // And add it back to the board list
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

    private Color opponent(Color player){
        if(player == Color.WHITE){
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    private ChessPiece getKing(Color kingsColor){
        List<Piece> playerPieces = boardPieces.stream().filter(x->((ChessPiece)x).getColor() == kingsColor).collect(Collectors.toList());
        for(Piece piece : playerPieces){
            if(piece instanceof King){
                return (ChessPiece)piece;
            }
        }
        throw new IllegalStateException("The is no " + kingsColor + " king on the board.");
    }

    private boolean testCheck(Color player){
        Position kingsPosition = getKing(player).getChessPosition().toPosition();
        List<Piece> opponentsPieces = boardPieces.stream().filter(x->((ChessPiece)x).getColor() == opponent(player)).collect(Collectors.toList());

        for(Piece piece : opponentsPieces){
            boolean[][] possibleMoves = piece.possibleMoves();
            if(possibleMoves[kingsPosition.getRow()][kingsPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color player){
        // 1. Make a list of all the pieces available for the player in check
        List<Piece> playerPieces = boardPieces.stream().filter(x->((ChessPiece)x).getColor() == player).collect(Collectors.toList());

        for(Piece piece : playerPieces){
            // 2. For each piece available for that player, get a map of its possible moves
            boolean[][] possibleMoves = piece.possibleMoves();
            Position source = ((ChessPiece)piece).getChessPosition().toPosition();

            for(int i=0; i<possibleMoves.length; i++){
                for(int j=0; j<possibleMoves.length; j++){

                    if(possibleMoves[i][j]){
                        // 3. For every possible position (target), make the move
                        Position tryTarget = new Position(i,j);
                        Piece capturedPiece = makeMove(source, tryTarget);
                        // 4. Test if that move still resulted in check
                        boolean check = testCheck(player);
                        // 5. No matter the results, undo the move
                        undoMove(source, tryTarget, capturedPiece);

                        // If at least one move doesn't result in check, it's not checkmate
                        if(!check)
                            return false;
                    }
                }
            }
        }

        // If every possible move available for that player still results in check, then it's checkmate
        return true;
    }

    private void placeNewPiece(char column, int row, ChessPiece chessPiece){
        BOARD.placePiece(chessPiece, new ChessPosition(column, row).toPosition());
        boardPieces.add(chessPiece);
    }

    private void initialSetup(){
        placeNewPiece('a',2, new Queen(BOARD, Color.BLACK));
        placeNewPiece('c', 2, new Knight(BOARD, Color.BLACK));
        placeNewPiece('g', 8, new Knight(BOARD, Color.BLACK));
        placeNewPiece('c', 4, new Bishop(BOARD, Color.BLACK));
        placeNewPiece('b', 8, new Bishop(BOARD, Color.BLACK));
        placeNewPiece('c', 1, new King(BOARD, Color.WHITE));
        placeNewPiece('e', 8, new King(BOARD, Color.BLACK));
    }

}
