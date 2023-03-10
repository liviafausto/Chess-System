package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {

    // Colors for computer terminal
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";


    public static void clearScreen() {
        // https://stackoverflow.com/questions/2979383/java-clear-the-console
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPosition readChessPosition(Scanner read){
        try{
            String chessPosition = read.nextLine();

            char column = chessPosition.charAt(0);
            int row = Integer.parseInt(chessPosition.substring(1));

            return new ChessPosition(column, row);
        }
        catch(RuntimeException invalidPosition){
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
        }
    }

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured){
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.print("Turn " + chessMatch.getTurn());

        if(!chessMatch.getCheckmate()){
            System.out.print(" - Waiting " + chessMatch.getCurrentPlayer() + " player...\n");

            if(chessMatch.getCheck())
                System.out.print(chessMatch.getCurrentPlayer() + " KING IS IN CHECK!");
        }
        else {
            System.out.println();
            System.out.println("CHECKMATE!");
            System.out.println(chessMatch.getCurrentPlayer() + " wins");
        }
    }

    public static void printBoard(ChessPiece[][] chessPieces){

        for(int i=0; i<chessPieces.length; i++){
            System.out.print((8-i) + " | ");
            for(int j=0; j<chessPieces.length; j++){
                printPiece(chessPieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println("    a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] chessPieces, boolean[][] possibleMoves){

        for(int i=0; i<chessPieces.length; i++){
            System.out.print((8-i) + " | ");
            for(int j=0; j<chessPieces.length; j++){
                printPiece(chessPieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("    a b c d e f g h");
    }

    private static void printPiece(ChessPiece chessPiece, boolean background){
        if(background){
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if(chessPiece == null){
            System.out.print("-" + ANSI_RESET);
        } else {
            if(chessPiece.getColor() == Color.WHITE){
                System.out.print(ANSI_WHITE + chessPiece + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + chessPiece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    private static void printCapturedPieces(List<ChessPiece> captured){
        List<ChessPiece> whiteCaptured = captured.stream().filter(x->x.getColor() == Color.WHITE).collect(Collectors.toList());
        List<ChessPiece> blackCaptured = captured.stream().filter(x->x.getColor() == Color.BLACK).collect(Collectors.toList());

        System.out.println("CAPTURED PIECES: ");
        System.out.print("White: ");
        System.out.print(ANSI_WHITE);
        System.out.print(Arrays.toString(whiteCaptured.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print("\nBlack: ");
        System.out.print(ANSI_YELLOW);
        System.out.print(Arrays.toString(blackCaptured.toArray()));
        System.out.print(ANSI_RESET);
        System.out.println();
    }
}
