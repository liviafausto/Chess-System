package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {
    public Bishop(Board board, Color color){
        super(board, color);
    }

    @Override
    public String toString(){
        return "B";
    }



    @Override
    public boolean[][] possibleMoves(){
        // The Bishop moves only diagonally
        boolean[][] bishopsMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // 1. Moves towards the northwest
        considerMoveTowards(bishopsMoves, -1, -1);

        // 2. Moves towards the northeast
        considerMoveTowards(bishopsMoves, -1, +1);

        // 3. Moves towards the southwest
        considerMoveTowards(bishopsMoves, +1, -1);

        // 4. Moves towards the southeast
        considerMoveTowards(bishopsMoves, +1, +1);

        return bishopsMoves;
    }
}
