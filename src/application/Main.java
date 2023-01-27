package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();

        while(true){
            UI.printBoard(chessMatch.getPieces());

            System.out.println();
            System.out.println("Your turn: move a piece");
            System.out.println();

            System.out.print("Source: ");
            ChessPosition sourcePosition = UI.readChessPosition(scanner);
            System.out.print("Target: ");
            ChessPosition targetPosition = UI.readChessPosition(scanner);

            ChessPiece capturedPiece = chessMatch.performChessMove(sourcePosition, targetPosition);
            if(capturedPiece != null){
                System.out.println();
                System.out.println("You captured " + capturedPiece);
            }
            System.out.println();
        }
    }
}