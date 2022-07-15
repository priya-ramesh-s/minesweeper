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

import minesweeper.Position;
import minesweeper.PositionStatus;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the Game class.
 * @author Alonso del Arte
 */
public class GameTest {
    
    private static final String RESET = "\u001B\u005B0m";
    private static final String RED = "\u001B\u005B31m";
    private static final String GREEN = "\u001B\u005B32m";
    private static final String YELLOW = "\u001B\u005B33m";

    /**
     * Test of the parsePosition function, of class Game. Since the board is 
     * supposed to be 10 &times; 26, with <i>x</i> labeled by a single digit and 
     * <i>y</i> labeled by a single letter (from A to Z), the combination of a 
     * letter and a digit should be recognized as referring to the appropriate 
     * position on the board.
     */
    @Test
    public void testParsePosition() {
        System.out.println("parsePosition");
        String s;
        Position expected, actual;
        for (char letter = 'A'; letter < '\u005B'; letter += '\u0001') {
            for (char digit = '0'; digit < ':'; digit += '\u0001') {
                s = "" + letter + digit;
                expected = new Position(digit - 48, letter - 65);
                actual = Game.parsePosition(s);
                assertEquals(expected, actual);
            }
        }
    }

    /**
     * Another test of the parsePosition function, of class Game. Since it's 
     * entirely possible that the player might refer to a position using a digit 
     * and letter combination rather than a letter and digit combination, the 
     * parsing function should be able to recognize such a combination as valid.
     */
    @Test
    public void testParsePositionDigitLetter() {
        String s;
        Position expected, actual;
        for (char letter = 'A'; letter < '\u005B'; letter += '\u0001') {
            for (char digit = '0'; digit < ':'; digit += '\u0001') {
                s = "" + digit + letter;
                expected = new Position(digit - 48, letter - 65);
                actual = Game.parsePosition(s);
                assertEquals(expected, actual);
            }
        }
    }
    
    // TODO: Reassess this test
    @Test
    public void testParsePositionCatchesBadCoords() {
        String badPositionSpec = "06";
        try {
            Position badPosition = Game.parsePosition(badPositionSpec);
            Position boundingCorner = new Position(9, 25);
            String msg = "Parsing invalid position " + badPositionSpec 
                    + " should have substituted positive out of bounds position";
            assert !badPosition.isWithinBounds(boundingCorner) : msg;
        } catch (IllegalArgumentException iae) {
            String msg = "Parsing invalid position " + badPositionSpec 
                    + " should have substituted rather than allowed exception";
            System.out.println(msg);
            System.out.println("\"" + iae.getMessage() + "\"");
            fail(msg);
        }
    }
    
    /**
     * Test of the chooseColor function, of class Game.
     */
    @Test
    public void testChooseColor() {
        System.out.println("chooseColor");
        assertEquals("?", Game.chooseColor(PositionStatus.COVERED));
        assertEquals(" ", Game.chooseColor(PositionStatus.REVEALED_EMPTY));
        assertEquals(YELLOW + "1" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_1));
        assertEquals(YELLOW + "2" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_2));
        assertEquals(YELLOW + "3" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_3));
        assertEquals(YELLOW + "4" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_4));
        assertEquals(YELLOW + "5" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_5));
        assertEquals(YELLOW + "6" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_6));
        assertEquals(YELLOW + "7" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_7));
        assertEquals(YELLOW + "8" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_EMPTY_NEAR_8));
        assertEquals(GREEN + "!" + RESET, 
                Game.chooseColor(PositionStatus.FLAGGED));
        assertEquals(RED + "x" + RESET, 
                Game.chooseColor(PositionStatus.REVEALED_MINED));
        assertEquals(RED + "X" + RESET, 
                Game.chooseColor(PositionStatus.DETONATED));
        assertEquals(RED + "w" + RESET, 
                Game.chooseColor(PositionStatus.WRONGLY_FLAGGED));
    }

    /**
     * Test of the main procedure, of class Game.
     */
    @Test
    public void testMain() {
        System.out.println("main");
//        String[] args = null;
//        Game.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
