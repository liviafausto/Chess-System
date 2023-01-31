package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "R";
    }

    @Override
    public boolean[][] possibleMoves() {
        // The Rook can move in its row or column
        boolean[][] rooksMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // 1. Moves above
        considerMoveTowards(rooksMoves, -1, 0);

        // 2. Moves below
        considerMoveTowards(rooksMoves, +1, 0);

        // 3. Moves to the left
        considerMoveTowards(rooksMoves, 0, -1);

        // 4. Moves to the right
        considerMoveTowards(rooksMoves, 0, +1);

        return rooksMoves;
    }
}
