package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {
    public Queen(Board board, Color color){
        super(board, color);
    }

    @Override
    public String toString(){
        return "Q";
    }

    @Override
    public boolean[][] possibleMoves(){
        // The Queen can move in all directions - in its rows, columns or diagonally
        boolean[][] queensMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // 1. Moves above
        considerMoveTowards(queensMoves, -1, 0);

        // 2. Moves below
        considerMoveTowards(queensMoves, +1, 0);

        // 3. Moves to the left
        considerMoveTowards(queensMoves, 0, -1);

        // 4. Moves to the right
        considerMoveTowards(queensMoves, 0, +1);

        // 5. Moves towards the northwest
        considerMoveTowards(queensMoves, -1, -1);

        // 6. Moves towards the northeast
        considerMoveTowards(queensMoves, -1, +1);

        // 7. Moves towards the southwest
        considerMoveTowards(queensMoves, +1, -1);

        // 8. Moves towards the southeast
        considerMoveTowards(queensMoves, +1, +1);

        return queensMoves;
    }
}
