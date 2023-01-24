package boardgame;

public class Board {
    private final int ROWS;
    private final int COLUMNS;
    private Piece[][] pieces;
    public Board(int ROWS, int COLUMNS){
        if(ROWS < 1 || COLUMNS < 1){
            throw new BoardException("Error in creating board: there must be at least 1 row and 1 column.");
        }

        this.ROWS = ROWS;
        this.COLUMNS = COLUMNS;
        pieces = new Piece[ROWS][COLUMNS];
    }

    public int getRows() { return ROWS; }

    public int getColumns() { return COLUMNS; }

    public Piece piece(int row, int column){
        if(!positionExists(row, column)){
            throw new BoardException("Position not on the board.");
        }
        return pieces[row][column];
    }

    public Piece piece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Position not on the board.");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("There is already a piece on position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExists(int row, int column){
        return row >= 0 && row < ROWS && column >=0 && column < COLUMNS;
    }

    public boolean positionExists(Position position){
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position){
        return piece(position) != null;
    }

}
