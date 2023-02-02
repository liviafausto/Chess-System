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

        // # Special move: Castling
        if(sourcePiece instanceof King){
            // The Castling happens when a King moves two squares to the right or left
            boolean kingSideCastling = (target.getColumn() == source.getColumn()+2);
            boolean queenSideCastling = (target.getColumn() == source.getColumn()-2);
            Position rooksSource, rooksTarget;

            if(kingSideCastling){
                // The Castling on the king-side uses the right Rook
                rooksSource = new Position(source.getRow(), source.getColumn()+3);
                // The Rook must be placed one square to the right of the King's source position
                rooksTarget = new Position(source.getRow(), source.getColumn()+1);

                ChessPiece rightRook = (ChessPiece) BOARD.removePiece(rooksSource);
                BOARD.placePiece(rightRook, rooksTarget);
                rightRook.increaseMoveCount();
            }
            else if(queenSideCastling){
                // The Castling on the queen-side uses the left Rook
                rooksSource = new Position(source.getRow(), source.getColumn()-4);
                // The Rook must be placed one square to the left of the King's source position
                rooksTarget = new Position(source.getRow(), source.getColumn()-1);

                ChessPiece leftRook = (ChessPiece) BOARD.removePiece(rooksSource);
                BOARD.placePiece(leftRook, rooksTarget);
                leftRook.increaseMoveCount();
            }

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

        // # Special move: Castling
        if(sourcePiece instanceof King) {
            // The Castling happenned if a King moved two squares to the right or left
            boolean kingSideCastling = (target.getColumn() == source.getColumn() + 2);
            boolean queenSideCastling = (target.getColumn() == source.getColumn() - 2);
            Position rooksSource, castlingPosition;

            if (kingSideCastling) {
                // The right-side Rook must be placed back to its source position
                rooksSource = new Position(source.getRow(), source.getColumn() + 3);
                castlingPosition = new Position(source.getRow(), source.getColumn() + 1);

                ChessPiece rightRook = (ChessPiece) BOARD.removePiece(castlingPosition);
                BOARD.placePiece(rightRook, rooksSource);
                rightRook.decreaseMoveCount();
            } else if (queenSideCastling) {
                // The left-side Rook must be placed back to its source position
                rooksSource = new Position(source.getRow(), source.getColumn() - 4);
                castlingPosition = new Position(source.getRow(), source.getColumn() - 1);

                ChessPiece leftRook = (ChessPiece) BOARD.removePiece(castlingPosition);
                BOARD.placePiece(leftRook, rooksSource);
                leftRook.decreaseMoveCount();
            }
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
            throw new ChessException("There are no possible moves for the chosen piece.");
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
        placeNewPiece('a', 8, new Rook(BOARD, Color.BLACK));
        placeNewPiece('b', 8, new Knight(BOARD, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(BOARD, Color.BLACK));
        placeNewPiece('d', 8, new Queen(BOARD, Color.BLACK));
        placeNewPiece('e', 8, new King(BOARD, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(BOARD, Color.BLACK));
        placeNewPiece('g', 8, new Knight(BOARD, Color.BLACK));
        placeNewPiece('h', 8, new Rook(BOARD, Color.BLACK));

        for(char column='a'; column<'i'; column++){
            placeNewPiece(column, 7, new Pawn(BOARD, Color.BLACK));
        }


        for(char column='a'; column<'i'; column++){
            placeNewPiece(column, 2, new Pawn(BOARD, Color.WHITE));
        }

        placeNewPiece('a', 1, new Rook(BOARD, Color.WHITE));
        placeNewPiece('b', 1, new Knight(BOARD, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(BOARD, Color.WHITE));
        placeNewPiece('d', 1, new Queen(BOARD, Color.WHITE));
        placeNewPiece('e', 1, new King(BOARD, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(BOARD, Color.WHITE));
        placeNewPiece('g', 1, new Knight(BOARD, Color.WHITE));
        placeNewPiece('h', 1, new Rook(BOARD, Color.WHITE));
    }

}
