package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {
    public Knight(Board board, Color color){
        super(board, color);
    }

    @Override
    public String toString(){
        return "N";
    }

    @Override
    public boolean[][] possibleMoves(){
        // The Knight move in "L-shapes" formats
        boolean[][] knightsMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        final int KNIGHTS_ROW = this.position.getRow();
        final int KNIGHTS_COLUMN = this.position.getColumn();

        Position possibleMove = new Position(0,0);

        // 1. One square up and two squares to the left
        possibleMove.setValues(KNIGHTS_ROW-1, KNIGHTS_COLUMN-2);
        considerMove(knightsMoves, possibleMove);

        // 2. One square up and two squares to the right
        possibleMove.setValues(KNIGHTS_ROW-1, KNIGHTS_COLUMN+2);
        considerMove(knightsMoves, possibleMove);

        // 3. Two squares up and one square to the left
        possibleMove.setValues(KNIGHTS_ROW-2, KNIGHTS_COLUMN-1);
        considerMove(knightsMoves, possibleMove);

        // 4. Two squares up and one square to the right
        possibleMove.setValues(KNIGHTS_ROW-2, KNIGHTS_COLUMN+1);
        considerMove(knightsMoves, possibleMove);

        // 5. One square down and two squares to the left
        possibleMove.setValues(KNIGHTS_ROW+1, KNIGHTS_COLUMN-2);
        considerMove(knightsMoves, possibleMove);

        // 6. One square down and two squares to the right
        possibleMove.setValues(KNIGHTS_ROW+1, KNIGHTS_COLUMN+2);
        considerMove(knightsMoves, possibleMove);

        // 7. Two squares down and one square to the left
        possibleMove.setValues(KNIGHTS_ROW+2, KNIGHTS_COLUMN-1);
        considerMove(knightsMoves, possibleMove);

        // 8. Two squares down and one square to the right
        possibleMove.setValues(KNIGHTS_ROW+2, KNIGHTS_COLUMN+1);
        considerMove(knightsMoves, possibleMove);

        return knightsMoves;
    }
}
