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
    // The Rook can move up, down, to the right and the left, as long as it's on a straight line
    public boolean[][] possibleMoves() {
        boolean[][] rooksMoves = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position possiblePosition = new Position(0,0);

        // 1. Looking above
        possiblePosition.setValues(this.position.getRow() - 1, this.position.getColumn());

        while(getBoard().positionExists(possiblePosition) && !getBoard().thereIsAPiece(possiblePosition)){
            // Looking for empty positions
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
            possiblePosition.setRow(possiblePosition.getRow() - 1);
        }
        if(getBoard().positionExists(possiblePosition) && isThereOpponentPiece(possiblePosition)){
            // There is a piece on this position, but it's an opponent
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
        }

        // 2. Looking to the left
        possiblePosition.setValues(this.position.getRow(), this.position.getColumn() - 1);

        while(getBoard().positionExists(possiblePosition) && !getBoard().thereIsAPiece(possiblePosition)){
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
            possiblePosition.setColumn(possiblePosition.getColumn() - 1);
        }
        if(getBoard().positionExists(possiblePosition) && isThereOpponentPiece(possiblePosition)){
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
        }

        // 3. Looking to the right
        possiblePosition.setValues(this.position.getRow(), this.position.getColumn() + 1);

        while(getBoard().positionExists(possiblePosition) && !getBoard().thereIsAPiece(possiblePosition)){
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
            possiblePosition.setColumn(possiblePosition.getColumn() + 1);
        }
        if(getBoard().positionExists(possiblePosition) && isThereOpponentPiece(possiblePosition)){
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
        }

        // 4. Looking below
        possiblePosition.setValues(this.position.getRow() + 1, this.position.getColumn());

        while(getBoard().positionExists(possiblePosition) && !getBoard().thereIsAPiece(possiblePosition)){
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
            possiblePosition.setRow(possiblePosition.getRow() + 1);
        }
        if(getBoard().positionExists(possiblePosition) && isThereOpponentPiece(possiblePosition)){
            rooksMoves[possiblePosition.getRow()][possiblePosition.getColumn()] = true;
        }

        return rooksMoves;
    }
}
