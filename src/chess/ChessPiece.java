package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {
    private Color color;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    protected boolean isThereOpponentPiece(Position position){
        ChessPiece opponentPiece = (ChessPiece) getBoard().getPiece(position);
        return opponentPiece != null && opponentPiece.getColor() != this.color;
    }

    protected void considerMove(boolean[][] map, Position target){
        // Considers whether moving to a specific target position is possible
        if(getBoard().positionExists(target))
            if (getBoard().getPiece(target) == null || isThereOpponentPiece(target))
                map[target.getRow()][target.getColumn()] = true;
    }

    protected void considerMoveTowards(boolean[][] map, int adjustRow, int adjustColumn){
        // Considers until which point it's possible to move towards a given direction
        final int SOURCE_ROW = this.position.getRow();
        final int SOURCE_COLUMN = this.position.getColumn();

        Position target = new Position(SOURCE_ROW + adjustRow, SOURCE_COLUMN + adjustColumn);

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

}
