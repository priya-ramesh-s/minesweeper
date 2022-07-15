/*
 * Copyright (C) 2020 Alonso del Arte
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

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests of the Position class.
 * @author Alonso del Arte
 */
public class PositionTest {
    
    private static final Random RANDOM = new Random();
    
    /**
     * Uses a pseudorandom number generator to choose a position on a 128 
     * &times; 128 board. This function is not meant for a production context.
     * @return A pseudorandomly chosen <code>Position</code>, one of (0, 0), (0, 
     * 127), (127, 0), (127, 127) or any position in the square area bounded by 
     * the aforementioned positions.
     */
    static Position makePosition() {
        int x = RANDOM.nextInt(128);
        int y = RANDOM.nextInt(128);
        return new Position(x, y);
    }
    
    /**
     * Test of getX getter, of class Position.
     */
    @Test
    public void testGetX() {
        System.out.println("getX");
        int expected = RANDOM.nextInt(720);
        Position position = new Position(expected, RANDOM.nextInt(480));
        int actual = position.getX();
        assertEquals(expected, actual);
    }
    
    /**
     * Test of getY getter, of class Position.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        int expected = RANDOM.nextInt(480);
        Position position = new Position(RANDOM.nextInt(720), expected);
        int actual = position.getY();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        System.out.println("toString");
        int x = RANDOM.nextInt(32);
        int y = RANDOM.nextInt(32);
        Position position = new Position(x, y);
        String expected = "(" + x + "," + y + ")";
        String actual = position.toString().replace(" ", "");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReferentialEquality() {
        Position position = makePosition();
        assertEquals(position, position);
    }
    
    @Test
    public void testNotEqualsNull() {
        Position position = makePosition();
        assertNotEquals(position, null);
    }
    
    @Test
    public void testNotEqualsDiffClass() {
        int x = RANDOM.nextInt(1920);
        int y = RANDOM.nextInt(1280);
        Position position = new Position(x, y);
        Point point = new Point(x, y);
        assertNotEquals(position, point);
    }
    
    @Test
    public void testNotEqualsDiffX() {
        int x = RANDOM.nextInt(1024);
        int y = RANDOM.nextInt(1024);
        int translation = RANDOM.nextInt(64) + 1;
        Position positionA = new Position(x, y);
        Position positionB = new Position(x + translation, y);
        assertNotEquals(positionA, positionB);
    }
    
    @Test
    public void testNotEqualsDiffY() {
        int x = RANDOM.nextInt(1024);
        int y = RANDOM.nextInt(1024);
        int translation = RANDOM.nextInt(64) + 1;
        Position positionA = new Position(x, y);
        Position positionB = new Position(x, y + translation);
        assertNotEquals(positionA, positionB);
    }
    
    @Test
    public void testEquals() {
        System.out.println("equals");
        int x = RANDOM.nextInt(960);
        int y = RANDOM.nextInt(720);
        Position somePosition = new Position(x, y);
        Position samePosition = new Position(x, y);
        assertEquals(somePosition, samePosition);
    }
    
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        HashSet<Position> positions = new HashSet<>();
        HashSet<Integer> hashes = new HashSet<>();
        Position curr;
        for (int x = 0; x < 32; x++)  {
            for (int y = 0; y < 32; y++) {
                curr = new Position(x, y);
                positions.add(curr);
                hashes.add(curr.hashCode());
            }
        }
        String msg = "Position set and hash code set should be the same size";
        assertEquals(msg, positions.size(), hashes.size());
    }
    
    @Test
    public void testIsWithinBounds() {
        System.out.println("isWithinBounds");
        int innerCornerCoord = 380;
        Position outerCorner = new Position(innerCornerCoord + 20, 
                innerCornerCoord + 20);
        int x = RANDOM.nextInt(innerCornerCoord);
        int y = RANDOM.nextInt(innerCornerCoord);
        Position position = new Position(x, y);
        String msg = "Position " + position.toString() 
                + " should be within bounds given by " + outerCorner.toString();
        assert position.isWithinBounds(outerCorner) : msg;
    }
    
    @Test
    public void testIsNotWithinBounds() {
        int innerCornerCoord = 380;
        Position outerCorner = new Position(innerCornerCoord + 20, 
                innerCornerCoord + 20);
        int x = RANDOM.nextInt(innerCornerCoord);
        int y = RANDOM.nextInt(innerCornerCoord * 7) + innerCornerCoord;
        Position position = new Position(x, y);
        String msg = "Position " + position.toString() 
                + " should not be within bounds given by " 
                + outerCorner.toString();
        assert !position.isWithinBounds(outerCorner) : msg;
    }
    
    @Test
    public void testGetNeighbors() {
        System.out.println("getNeighbors");
        Position position = new Position(8, 8);
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(7, 7));
        expected.add(new Position(7, 8));
        expected.add(new Position(7, 9));
        expected.add(new Position(8, 7));
        expected.add(new Position(8, 9));
        expected.add(new Position(9, 7));
        expected.add(new Position(9, 8));
        expected.add(new Position(9, 9));
        HashSet<Position> actual = position.getNeighbors();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetNeighborsZeroEdgePosition() {
        Position position = new Position(0, 7);
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(0, 6));
        expected.add(new Position(0, 8));
        expected.add(new Position(1, 6));
        expected.add(new Position(1, 7));
        expected.add(new Position(1, 8));
        try {
            HashSet<Position> actual = position.getNeighbors();
            assertEquals(expected, actual);
        } catch (IllegalArgumentException iae) {
            String msg = "getNeighbors failed to check for negative coordinates";
            System.out.println(msg);
            System.out.println("\"" + iae.getMessage() + "\"");
            fail(msg);
        }
    }
    
    @Test
    public void testGetNeighborsZeroCornerPosition() {
        Position position = new Position(0, 0);
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(0, 1));
        expected.add(new Position(1, 0));
        expected.add(new Position(1, 1));
        try {
            HashSet<Position> actual = position.getNeighbors();
            assertEquals(expected, actual);
        } catch (IllegalArgumentException iae) {
            String msg = "getNeighbors failed to check for negative coordinates";
            System.out.println(msg);
            System.out.println("\"" + iae.getMessage() + "\"");
            fail(msg);
        }
    }
    
    @Test
    public void testGetNeighborsBoundedEdgePosition() {
        Position position = new Position(10, 7);
        Position boundingCorner = new Position(10, 10);
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(9, 6));
        expected.add(new Position(9, 7));
        expected.add(new Position(9, 8));
        expected.add(new Position(10, 6));
        expected.add(new Position(10, 8));
        HashSet<Position> actual = position.getNeighbors(boundingCorner);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetNeighborsBoundedCornerPosition() {
        Position position = new Position(10, 10);
        Position boundingCorner = position;
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(9, 9));
        expected.add(new Position(9, 10));
        expected.add(new Position(10, 9));
        HashSet<Position> actual = position.getNeighbors(boundingCorner);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNextColumn() {
        System.out.println("nextColumn");
        int x = RANDOM.nextInt(384);
        int y = RANDOM.nextInt(256);
        Position position = new Position(x, y);
        Position expected = new Position(x, y + 1);
        Position actual = position.nextColumn();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNextColumnWithReset() {
        System.out.println("nextColumnWithReset");
        Position expected;
        Position actual = new Position(0, 0);
        int cornerCoord = 32;
        Position boundingCorner = new Position(cornerCoord, cornerCoord);
        for (int x = 0; x <= cornerCoord; x++) {
            for (int y = 0; y <= cornerCoord; y++) {
                expected = new Position(x, y);
                assertEquals(expected, actual);
                actual = actual.nextColumnWithReset(boundingCorner);
            }
        }
    }
    
    @Test
    public void testNextRow() {
        System.out.println("nextRow");
        int x = RANDOM.nextInt(384);
        int y = RANDOM.nextInt(256);
        Position position = new Position(x, y);
        Position expected = new Position(x + 1, y);
        Position actual = position.nextRow();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNextRowColumnZero() {
        System.out.println("nextRowColumnZero");
        int x = RANDOM.nextInt(384);
        int y = RANDOM.nextInt(256);
        Position position = new Position(x, y);
        Position expected = new Position(x + 1, 0);
        Position actual = position.nextRowColumnZero();
        assertEquals(expected, actual);
    }
    
    /**
     * Test of the random function of class Position. This does not test 
     * distribution, which is assumed to be uniform, only that the position is 
     * within the specified boundaries.
     */
    @Test
    public void testRandom() {
        Position boundingCorner, randomPosition;
        String msg;
        System.out.println("random");
        for (int x = 128; x < 256; x++) {
            for (int y = 128; y < 256; y++) {
                boundingCorner = new Position(x, y);
                randomPosition = Position.random(boundingCorner);
                msg = "Position " + randomPosition.toString() 
                        + " should be within bounds defined by " 
                        + boundingCorner.toString();
                assert randomPosition.isWithinBounds(boundingCorner) : msg;
            }
        }
    }
    
    @Test
    public void testConstructorRejectsNegativeX() {
        try {
            Position badPosition = new Position(-1, 0);
            String msg = "Should not have been able to create position " 
                    + badPosition.toString();
            fail(msg);
        } catch (IllegalArgumentException iae) {
            System.out.println("Position with negative x was correctly rejected");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for negative x";
            fail(msg);
        }
    }
    
    @Test
    public void testConstructorRejectsNegativeY() {
        try {
            Position badPosition = new Position(0, -1);
            String msg = "Should not have been able to create position " 
                    + badPosition.toString();
            fail(msg);
        } catch (IllegalArgumentException iae) {
            System.out.println("Position with negative y was correctly rejected");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for negative y";
            fail(msg);
        }
    }
    
    @Test
    public void testConstructorRejectsNegativeXAndY() {
        try {
            Position badPosition = new Position(-1, -1);
            String msg = "Should not have been able to create position " 
                    + badPosition.toString();
            fail(msg);
        } catch (IllegalArgumentException iae) {
            System.out.println("Position with negative x, y was correctly rejected");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for negative x, y";
            fail(msg);
        }
    }
    
}
