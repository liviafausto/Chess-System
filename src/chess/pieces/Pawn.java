package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    private final ChessMatch MATCH;
    public Pawn(Board board, Color color, ChessMatch MATCH){
        super(board, color);
        this.MATCH = MATCH;
    }

    @Override
    public String toString(){
        return "P";
    }

    private boolean considerMovePawn(boolean[][] map, Position target){
        // Considers whether the Pawn can move to a target square
        if(getBoard().positionExists(target) && !getBoard().thereIsAPiece(target)) {
            map[target.getRow()][target.getColumn()] = true;
            return true;
        } else
            return false;
    }

    private void considerPawnCapture(boolean[][] map, Position target){
        // Considers whether the Pawn can capture an opponent piece
        if(getBoard().positionExists(target) && isThereOpponentPiece(target))
            map[target.getRow()][target.getColumn()] = true;
    }

    // # SPECIAL MOVE - EN PASSANT
    // For this move, the Pawn can capture an opponent's Pawn one square to the right/left
    private void considerEnPassant(boolean[][] map, int adjustRow){
        final int PAWNS_ROW = this.position.getRow();
        final int PAWNS_COLUMN = this.position.getColumn();

        Position left = new Position(PAWNS_ROW, PAWNS_COLUMN - 1);
        Position right = new Position(PAWNS_ROW, PAWNS_COLUMN + 1);

        // 1. Test the En Passant one square to the left
        if(getBoard().positionExists(left) && isThereOpponentPiece(left)){
            ChessPiece leftOpponent = (ChessPiece) this.getBoard().getPiece(left);
            if(leftOpponent == this.MATCH.getEnPassantVulnerable())
                map[left.getRow() + adjustRow][left.getColumn()] = true;
        }

        // 2. Test the En Passant one square to the right
        if(getBoard().positionExists(right) && isThereOpponentPiece(right)){
            ChessPiece rightOpponent = (ChessPiece) this.getBoard().getPiece(right);
            if(rightOpponent == this.MATCH.getEnPassantVulnerable())
                map[right.getRow() + adjustRow][right.getColumn()] = true;
        }
    }


    @Override
    public boolean[][] possibleMoves(){
        // The Pawn moves one square straight ahead, but capture pieces diagonally
        boolean[][] pawnsMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        final int PAWNS_ROW = this.position.getRow();
        final int PAWNS_COLUMN = this.position.getColumn();

        Position possibleMove = new Position(0,0);
        int moveDirection;

        // 1. White Pawns move up and Black Pawns move down
        if(this.getColor() == Color.WHITE)
            moveDirection = -1;
        else
            moveDirection = +1;

        // 2. Move one square straight ahead
        possibleMove.setValues(PAWNS_ROW + moveDirection, PAWNS_COLUMN);
        boolean firstSquareIsFree = considerMovePawn(pawnsMoves, possibleMove);

        // 2.1 The Pawn can also move two squares ahead on its first move
        if(getMoveCount() == 0 && firstSquareIsFree){
            possibleMove.setValues(PAWNS_ROW + (2*moveDirection), PAWNS_COLUMN);
            considerMovePawn(pawnsMoves, possibleMove);
        }

        // 3. Capture on the left diagonal
        possibleMove.setValues(PAWNS_ROW + moveDirection, PAWNS_COLUMN-1);
        considerPawnCapture(pawnsMoves, possibleMove);

        // 4. Capture on the right diagonal
        possibleMove.setValues(PAWNS_ROW + moveDirection, PAWNS_COLUMN+1);
        considerPawnCapture(pawnsMoves, possibleMove);

        // # Special Move: En Passant
        // The White Pawn has to be on the third row
        if(this.getColor() == Color.WHITE && PAWNS_ROW == 3)
            considerEnPassant(pawnsMoves, moveDirection);

        // The Black Pawn has to be on the fourth row
        if(this.getColor() == Color.BLACK && PAWNS_ROW == 4)
            considerEnPassant(pawnsMoves, moveDirection);

        return pawnsMoves;
    }
}
