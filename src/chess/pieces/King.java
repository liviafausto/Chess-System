package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        // The King moves from its position to a neighboring position
        boolean[][] kingsMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        final int KINGS_ROW = this.position.getRow();
        final int KINGS_COLUMN = this.position.getColumn();

        Position possibleMove = new Position(0,0);

        // 1. Neighbor above
        possibleMove.setValues(KINGS_ROW - 1, KINGS_COLUMN);
        considerMove(kingsMoves, possibleMove);

        // 2. Neighbor below
        possibleMove.setValues(KINGS_ROW + 1, KINGS_COLUMN);
        considerMove(kingsMoves, possibleMove);

        // 3. Neighbor to the left
        possibleMove.setValues(KINGS_ROW, KINGS_COLUMN - 1);
        considerMove(kingsMoves, possibleMove);

        // 4. Neighbor to the right
        possibleMove.setValues(KINGS_ROW, KINGS_COLUMN + 1);
        considerMove(kingsMoves, possibleMove);

        // 5. Neighbor to the northwest
        possibleMove.setValues(KINGS_ROW - 1, KINGS_COLUMN - 1);
        considerMove(kingsMoves, possibleMove);

        // 6. Neighbor to the northeast
        possibleMove.setValues(KINGS_ROW - 1, KINGS_COLUMN + 1);
        considerMove(kingsMoves, possibleMove);

        // 7. Neighbor to the southwest
        possibleMove.setValues(KINGS_ROW + 1, KINGS_COLUMN - 1);
        considerMove(kingsMoves, possibleMove);

        // 8. Neighbor to the southeast
        possibleMove.setValues(KINGS_ROW + 1, KINGS_COLUMN + 1);
        considerMove(kingsMoves, possibleMove);

        return kingsMoves;
    }
}
