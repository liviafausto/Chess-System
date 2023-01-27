package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();

        while(true){
            try{
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());

                System.out.println();
                System.out.println();
                System.out.println("CHOOSE A POSITION TO MOVE");
                System.out.print("Source: ");
                ChessPosition sourcePosition = UI.readChessPosition(scanner);
                System.out.print("Target: ");
                ChessPosition targetPosition = UI.readChessPosition(scanner);

                ChessPiece capturedPiece = chessMatch.performChessMove(sourcePosition, targetPosition);
            }
            catch(ChessException chessException){
                System.out.println();
                System.out.println("Error: " + chessException.getMessage());
                System.out.print("Press enter to try again ");
                scanner.nextLine(); // Wait for user to press enter after exception
            }
            catch(InputMismatchException inputException){
                System.out.println();
                System.out.println("Error: " + inputException.getMessage());
                System.out.print("Press enter to try again ");
                scanner.nextLine();
            }
        }

    }

}