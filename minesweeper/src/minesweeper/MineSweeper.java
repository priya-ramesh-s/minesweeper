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
package minesweeper;

/**
 * WORK IN PROGRESS: This will at first route the player to play the game on the 
 * command line. Once the graphical version is ready, the command line version 
 * will still be accessible with a command line option.
 * @author Alonso del Arte
 */
public class MineSweeper {
    
    /**
     * Text for the Help &rarr; About... dialog box and the "-version" command 
     * line option.
     */
    public static final String VERSION_TEXT 
            = "MineSweeper\nVersion 0.1\n\u00A9 2020 Alonso del Arte";
    
    public static final int DEFAULT_BOARD_WIDTH = 48;
    public static final int DEFAULT_BOARD_HEIGHT = 32;
    
    private static int specifiedBoardWidth = 0;
    private static int specifiedBoardHeight = 0;
    
    private static boolean textGameFlag = true;
    private static boolean useANSIColors = false;
    
    private static String difficultyLevel = "-medium";
    
    private static void processCommandLineArgs(String[] args) {
        boolean nextNumberIsWidth = true;
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "-ansi":
                case "-ansicolors":
                case "-ansicolor":
                case "-color":
                case "-colors":
                    useANSIColors = true;
                    break;
                case "-easy":
                    difficultyLevel = "-easy";
                    break;
                case "-hard":
                    difficultyLevel = "-hard";
                    break;
                case "-h":
                case "-height":
                    nextNumberIsWidth = false;
                    break;
                case "-medium":
                    difficultyLevel = "-medium";
                    break;
                case "-t":
                case "-text":
                    textGameFlag = true;
                    break;
                case "-version":
                    System.out.println(VERSION_TEXT);
                    break;
                case "-w":
                case "-width":
                    nextNumberIsWidth = true;
                    break;
                default:
                    try {
                        int number = Integer.parseInt(arg);
                        if (nextNumberIsWidth) {
                            specifiedBoardWidth = number;
                        } else {
                            specifiedBoardHeight = number;
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("\"" + arg 
                                + "\" not recognized as a number nor option");
                        System.out.println("\"" + nfe.getMessage() + "\"");
                    }
            }
        }
    }

    /**
     * Entry point for the program.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        processCommandLineArgs(args);
        if (specifiedBoardWidth < 1) {
            specifiedBoardWidth = DEFAULT_BOARD_WIDTH;
        }
        if (specifiedBoardHeight < 1) {
            specifiedBoardHeight = DEFAULT_BOARD_HEIGHT;
        }
        String[] parsedArgs = new String[3];
        parsedArgs[0] = "width = " + specifiedBoardWidth;
        parsedArgs[1] = "height = " + specifiedBoardHeight;
        parsedArgs[2] = difficultyLevel;
        if (textGameFlag) {
            if (useANSIColors) {
                parsedArgs[0] = "-colors";
            }
            ui.text.Game.main(parsedArgs);
        } else {
            ui.graphical.Game.main(parsedArgs);
        }
    }
    
}
