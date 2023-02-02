package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    private final ChessMatch MATCH;

    public King(Board board, Color color, ChessMatch MATCH) {
        super(board, color);
        this.MATCH = MATCH;
    }

    @Override
    public String toString(){
        return "K";
    }


    // # SPECIAL MOVE - CASTLING:
    // The King moves two squares to the left/right and a Rook moves over and in front of the King
    private boolean testRookCastling(Position position){
        // Tests if a given position has a Rook which can perform the Castling
        ChessPiece rook = (ChessPiece)getBoard().getPiece(position);
        // The Rook can only perform the Castling on its first move of the match
        return rook instanceof Rook && rook.getColor() == this.getColor() && rook.getMoveCount() == 0;
    }

    private void testKingCastling(boolean[][] map){
        final int KINGS_ROW = this.position.getRow();
        final int KINGS_COLUMN = this.position.getColumn();

        // 1. Test Castling with the king-side (right side) Rook
        Position rightRook = new Position(KINGS_ROW, KINGS_COLUMN + 3);
        if(testRookCastling(rightRook)){
            Position oneToTheRight = new Position(KINGS_ROW, KINGS_COLUMN + 1);
            Position twoToTheRight = new Position(KINGS_ROW, KINGS_COLUMN + 2);
            if(!getBoard().thereIsAPiece(oneToTheRight) && !getBoard().thereIsAPiece(twoToTheRight)){
                map[KINGS_ROW][KINGS_COLUMN + 2] = true;
            }
        }

        // 2. Test Castling with the queen-side (left side) Rook
        Position leftRook = new Position(KINGS_ROW, KINGS_COLUMN - 4);
        if(testRookCastling(leftRook)){
            Position oneToTheLeft = new Position(KINGS_ROW, KINGS_COLUMN - 1);
            Position twoToTheLeft = new Position(KINGS_ROW, KINGS_COLUMN - 2);
            Position threeToTheLeft = new Position(KINGS_ROW, KINGS_COLUMN - 3);
            if(!getBoard().thereIsAPiece(oneToTheLeft) && !getBoard().thereIsAPiece(twoToTheLeft) && !getBoard().thereIsAPiece(threeToTheLeft)){
                map[KINGS_ROW][KINGS_COLUMN - 2] = true;
            }
        }
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

        // # Special Move: Castling
        // The King has to be on its first move of the match, and it can't be in check for the Castling
        if(getMoveCount() == 0 && !MATCH.getCheck()){
            testKingCastling(kingsMoves);
        }

        return kingsMoves;
    }
}
