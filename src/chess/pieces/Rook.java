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

    private void considerMove(boolean[][] map, int adjustRow, int adjustColumn){
        final int ROOKS_ROW = this.position.getRow();
        final int ROOKS_COLUMN = this.position.getColumn();

        Position target = new Position(ROOKS_ROW + adjustRow, ROOKS_COLUMN + adjustColumn);

        while(getBoard().positionExists(target) && !getBoard().thereIsAPiece(target)){
            // Looking through empty positions
            map[target.getRow()][target.getColumn()] = true;
            target.setValues(target.getRow() + adjustRow, target.getColumn() + adjustColumn);
        }
        if(getBoard().positionExists(target) && isThereOpponentPiece(target)){
            // There is a piece on this position, but it's an opponent
            map[target.getRow()][target.getColumn()] = true;
        }

    }

    @Override
    public boolean[][] possibleMoves() {
        // The Rook can move in its row or column
        boolean[][] rooksMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];

        // 1. Moves above
        considerMove(rooksMoves, -1, 0);

        // 2. Moves below
        considerMove(rooksMoves, +1, 0);

        // 3. Moves to the left
        considerMove(rooksMoves, 0, -1);

        // 4. Moves to the right
        considerMove(rooksMoves, 0, +1);

        return rooksMoves;
    }
}
