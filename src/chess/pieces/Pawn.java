package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color){
        super(board, color);
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

    @Override
    public boolean[][] possibleMoves(){
        // The Pawn moves one square straight ahead, but capture pieces diagonally
        // The Pawn can also move two squares ahead on its first move
        boolean[][] pawnsMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        final int PAWNS_ROW = this.position.getRow();
        final int PAWNS_COLUMN = this.position.getColumn();

        Position possibleMove = new Position(0,0);
        boolean firstSquareIsFree;

        if(this.getColor() == Color.WHITE){
            // 1. White Pawns move up
            possibleMove.setValues(PAWNS_ROW - 1, PAWNS_COLUMN);
            firstSquareIsFree = considerMovePawn(pawnsMoves, possibleMove);
            if(getMoveCount() == 0 && firstSquareIsFree){
                possibleMove.setValues(PAWNS_ROW - 2, PAWNS_COLUMN);
                considerMovePawn(pawnsMoves, possibleMove);
            }

            // Capture on the northwest diagonal
            possibleMove.setValues(PAWNS_ROW - 1, PAWNS_COLUMN - 1);
            considerPawnCapture(pawnsMoves, possibleMove);

            // Capture on the northeast diagonal
            possibleMove.setValues(PAWNS_ROW - 1, PAWNS_COLUMN + 1);
            considerPawnCapture(pawnsMoves, possibleMove);
        }
        else {
            // 2. Black Pawns move down
            possibleMove.setValues(PAWNS_ROW + 1, PAWNS_COLUMN);
            firstSquareIsFree = considerMovePawn(pawnsMoves, possibleMove);
            if(getMoveCount() == 0 && firstSquareIsFree){
                possibleMove.setValues(PAWNS_ROW + 2, PAWNS_COLUMN);
                considerMovePawn(pawnsMoves, possibleMove);
            }

            // Capture on the southwest diagonal
            possibleMove.setValues(PAWNS_ROW + 1, PAWNS_COLUMN - 1);
            considerPawnCapture(pawnsMoves, possibleMove);

            // Capture on the southeast diagonal
            possibleMove.setValues(PAWNS_ROW + 1, PAWNS_COLUMN + 1);
            considerPawnCapture(pawnsMoves, possibleMove);
        }

        return pawnsMoves;
    }
}
