package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> capturedPieces = new ArrayList<>();

        while(!chessMatch.getCheckmate()){
            try{
                UI.clearScreen();
                UI.printMatch(chessMatch, capturedPieces);
                System.out.println();
                System.out.print("Source: ");
                ChessPosition sourcePosition = UI.readChessPosition(scanner);

                // The possible moves on the board have colored background:
                boolean[][] possibleMoves = chessMatch.possibleMoves(sourcePosition);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.println();
                System.out.print("Target: ");
                ChessPosition targetPosition = UI.readChessPosition(scanner);

                ChessPiece capturedPiece = chessMatch.performChessMove(sourcePosition, targetPosition);

                if(capturedPiece != null){
                    capturedPieces.add(capturedPiece);
                    /*System.out.println();
                    System.out.println("CONGRATULATIONS, YOU CAPTURED A PIECE!");
                    System.out.print("Press enter continue ");
                    scanner.nextLine();*/
                }

                if(chessMatch.getPromotedPiece() != null){
                    System.out.println();
                    System.out.print("Choose a piece for promotion (B/N/R/Q): ");
                    String type = scanner.nextLine();
                    boolean replaced = chessMatch.replacePromotedPiece(type);

                    while(!replaced){
                        System.out.print("Invalid value! Enter piece for promotion (B/N/R/Q): ");
                        type = scanner.nextLine();
                        replaced = chessMatch.replacePromotedPiece(type);
                    }
                }

            }
            catch(ChessException chessException){
                System.out.println();
                System.out.println(chessException.getMessage());
                System.out.print("Press enter to try again ");
                scanner.nextLine(); // Wait for user to press enter after exception
            }
            catch(InputMismatchException inputException){
                System.out.println();
                System.out.println(inputException.getMessage());
                System.out.print("Press enter to try again ");
                scanner.nextLine();
            }
        }

        // The while loop has been broken: the game has reached checkmate
        UI.clearScreen();
        UI.printMatch(chessMatch, capturedPieces);
        System.out.println();
        System.out.println("Thanks for playing!!!  ;)");
    }

}