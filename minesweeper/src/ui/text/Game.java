/*
 * Copyright (C) 2021 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ui.text;

import minesweeper.Board;
import minesweeper.Mine;
import minesweeper.Position;
import minesweeper.PositionStatus;

import java.util.Optional;
import java.util.Scanner;

/**
 * Play the Minesweeper game on the command line. The board is 10 &times; 26.
 * @author Alonso del Arte
 */
public class Game {
    
    private static Board gameBoard;
    
    private static Position currPos = new Position(0, 0);
    
    private static final Position MAXIMUM_POSITION = new Position(9, 25);
    
    private static int numberOfMines;
    
    private static Mine detonatedMine = null;
    
    private static final String ALPHABET
            = "  A B C D E F G H I J K L M N O P Q R S T U V W X Y Z";
    
    private static boolean useANSIColors = false;
    
    private static final String ANSI_RESET = "\u001B\u005B0m";
    private static final String ANSI_RED = "\u001B\u005B31m";
    private static final String ANSI_GREEN = "\u001B\u005B32m";
    private static final String ANSI_YELLOW = "\u001B\u005B33m";

    private static void writeBoardNoColors() {
        Position curr;
        PositionStatus status;
        for (int x = 0; x < 10; x++) {
            System.out.print(x + " ");
            for (int y = 0; y < 26; y++) {
                curr = new Position(x, y);
                status = gameBoard.query(curr);
                System.out.print(status.getChar() + " ");
            }
            System.out.println(x);
            System.out.println();
        }
    }

    /**
     * Chooses ANSI codes to display some game elements in colors. Note that 
     * this does not work on the Windows command prompt.
     * @param status The status for which to choose a color. For example, {@link 
     * PositionStatus#FLAGGED}.
     * @return A <code>String</code> beginning with an ANSI code and an ANSI 
     * reset when applicable. For example, for <code>FLAGGED</code> this would 
     * return the ANSI code for green, followed by the exclamation mark 
     * character followed by the ANSI reset cancelling green.
     */
    static String chooseColor(PositionStatus status) {
        String symbol;
        switch (status) {
            case COVERED:
                symbol = "?";
                break;
            case REVEALED_EMPTY:
                symbol = " ";
                break;
            case REVEALED_EMPTY_NEAR_1:
            case REVEALED_EMPTY_NEAR_2:
            case REVEALED_EMPTY_NEAR_3:
            case REVEALED_EMPTY_NEAR_4:
            case REVEALED_EMPTY_NEAR_5:
            case REVEALED_EMPTY_NEAR_6:
            case REVEALED_EMPTY_NEAR_7:
            case REVEALED_EMPTY_NEAR_8:
                symbol = ANSI_YELLOW + status.getChar() + ANSI_RESET;
                break;
            case FLAGGED:
                symbol = ANSI_GREEN + '!' + ANSI_RESET;
                break;
            case REVEALED_MINED:
            case DETONATED:
            case WRONGLY_FLAGGED:
                symbol = ANSI_RED + status.getChar() + ANSI_RESET;
                break;
            default:
                String excMsg = "Status " + status.name() + " not recognized";
                throw new RuntimeException(excMsg);
        }
        return symbol;
    }
    
    private static void writeBoardWithColors() {
        Position curr;
        PositionStatus status;
        for (int x = 0; x < 10; x++) {
            System.out.print(x + " ");
            for (int y = 0; y < 26; y++) {
                curr = new Position(x, y);
                status = gameBoard.query(curr);
                System.out.print(chooseColor(status) + " ");
            }
            System.out.println(x);
            System.out.println();
        }
    }
    
    private static void writeBoard() {
        System.out.println(ALPHABET);
        System.out.println();
        if (useANSIColors) {
            writeBoardWithColors();
        } else {
            writeBoardNoColors();
        }
        System.out.println(ALPHABET);
        System.out.println();
    }
    
    private static void writeHelp() {
        System.out.println("Use position's letter and number, e.g., A0");
        System.out.println("Type \"flag\" and then position to flag");
        System.out.println("Type \"unflag\" and then position to unflag");
        System.out.println("Type position by itself to reveal");
        System.out.println();
    }
    
    /**
     * Parses a <code>String</code> to determine a <code>Position</code> on the 
     * 10 &times; 26 board. This function is package private, rather than fully 
     * class private, to enable unit testing.
     * @param s The <code>String</code> to parse. For example, "A7".
     * @return The corresponding <code>Position</code>. For example, (7, 0).
     * @throws IllegalArgumentException If parsing leads to an attempt to 
     * construct a <code>Position</code> object with negative coordinates.
     */
    static Position parsePosition(String s) {
        char letter = s.charAt(0);
        char digit = s.charAt(1);
        if (Character.isAlphabetic(digit)) {
            char swap = letter;
            letter = digit;
            digit = swap;
        }
        int x = digit - 48;
        int y = letter - 65;
        if (x < 0) x = -x + 10;
        if (y < 0) y = -y + 26;
        return new Position(x, y);
    }
    
    private static String parseCommand(String command) {
        command = command.replace(" ", "").toUpperCase();
        String posStr = "";
        if (command.contains("UNFLAG")) {
            posStr = command.replace("UNFLAG", "");
            command = "unflag";
        }
        if (command.contains("FLAG")) {
            posStr = command.replace("FLAG", "");
            command = "FLAG";
        }
        if (command.contains("REVEAL")) {
            posStr = command.replace("REVEAL", "");
            command = "REVEAL";
        }
        if (command.length() == 2) {
            posStr = command;
            command = "REVEAL";
        }
        if (posStr.length() == 2) {
            currPos = parsePosition(posStr);
        }
        return command;
    }
    
    private static void processCommand(String command) {
        command = parseCommand(command);
        if (currPos.isWithinBounds(MAXIMUM_POSITION)) {
            PositionStatus status = gameBoard.query(currPos);
            switch (command) {
                case "REVEAL":
                    if (status.equals(PositionStatus.COVERED)) {
                        Optional<Mine> option = gameBoard.reveal(currPos);
                        if (option.isPresent()) {
                            detonatedMine = option.get();
                        }
                    } else {
                        System.out.println("That position is already uncovered");
                    }
                    break;
                case "FLAG":
                    if (status.equals(PositionStatus.COVERED)) {
                        gameBoard.flag(currPos);
                    } else {
                        System.out.println("That position can't be flagged, it is " 
                                + status.toString());
                    }
                    break;
                case "unflag":
                    if (status.equals(PositionStatus.FLAGGED)) {
                        gameBoard.unflag(currPos);
                    } else {
                        System.out.println("That position is " 
                                + status.toString()
                                + ", so it can't be unflagged");
                    }
                    break;
                case "????":
                    writeHelp();
                    break;
                default:
                    System.out.println("Sorry, command \"" + command 
                            + "\" not recognized");
            }
        } else {
            System.out.println("Sorry, position " + currPos.toString()
                    + " is out of bounds");}
    }
    
    public static void main(String[] args) {
        System.out.println();
        System.out.println("MINESWEEPER");
        System.out.println();
        if (args.length > 2) {
            switch (args[2].toLowerCase()) {
                case "-easy":
                    numberOfMines = 10;
                    break;
                case "-hard":
                    numberOfMines = 52;
                    break;
                case "-medium":
                default:
                    numberOfMines = 26;
            }
        }
        gameBoard = Board.makeBoard(numberOfMines, MAXIMUM_POSITION);
        if (args.length > 0) {
            useANSIColors = args[0].startsWith("-c");
        }
        writeBoard();
        writeHelp();
        String cmd;
        try (Scanner input = new Scanner(System.in)) {
            while (gameBoard.gameUnderway()) {
                System.out.print("> ");
                cmd = input.nextLine();
                System.out.println();
                processCommand(cmd);
                writeBoard();
            }
        }
        if (gameBoard.gameWon()) {
            if (useANSIColors) {
                System.out.println(ANSI_GREEN);
            }
            System.out.println("Congratulations, YOU WON!");
            if (useANSIColors) {
                System.out.println(ANSI_RESET);
            }
        } else {
            if (useANSIColors) {
                System.out.println(ANSI_RED);
            }
            System.out.println("Oops, sorry, you detonated " 
                    + detonatedMine.toString());
            if (useANSIColors) {
                System.out.println(ANSI_RESET);
            }
            System.out.println("Better luck next time.");
        }
    }
    
}
