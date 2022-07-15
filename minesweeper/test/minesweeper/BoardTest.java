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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the Board class.
 * @author Alonso del Arte
 */
public class BoardTest {
    
    private static final Position POSITION_ZERO = new Position(0, 0);
    
    /**
     * Test of query function, of class Board. At the beginning of the game, all 
     * positions should be covered.
     */
    @Test
    public void testInitialQuery() {
        Position maxPos = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        Board board = new Board(maxPos, mineLocs);
        for (Position curr = POSITION_ZERO;
                curr.isWithinBounds(POSITION_ZERO);
                curr = curr.nextColumnWithReset(maxPos)) {
            assertEquals(PositionStatus.COVERED, board.query(curr));
        }
    }
    
    /**
     * Test of query function, of class Board.
     */
    @Test
    public void testQuery() {
        System.out.println("query");
        Position centerSquare = POSITION_ZERO.nextColumn().nextRow();
        Position sizingCorner = centerSquare.nextColumn().nextRow();
        HashSet<Position> edgeSquares = centerSquare.getNeighbors();
        ArrayList<Position> neighbors = new ArrayList<>(edgeSquares);
        HashSet<Position> mineLocs;
        Board board;
        Optional<Mine> option;
        PositionStatus[] statuses = PositionStatus.values();
        for (int n = 0; n <= edgeSquares.size(); n++) {
            mineLocs = new HashSet<>(neighbors.subList(0, n));
            board = new Board(sizingCorner, mineLocs);
            option = board.reveal(centerSquare);
            assert !option.isPresent() : "Center square should be empty";
            assertEquals(statuses[n], board.query(centerSquare));
        }
    }
    
    /**
     * Another test of query function, of class Board. Queries for out of bounds 
     * positions should cause <code>NoSuchElementException</code>.
     */
    @Test
    public void testNoQueryOutOfBounds() {
        Position nextToCorner = PositionTest.makePosition();
        Position corner = nextToCorner.nextColumn();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(nextToCorner);
        mineLocs.add(corner);
        Board board = new Board(corner, mineLocs);
        Position outOfBounds = corner.nextColumn().nextRow();
        try {
            PositionStatus badStatus = board.query(outOfBounds);
            String msg = "Trying to query position " + outOfBounds.toString() 
                    + ", which is beyond corner " + corner.toString() 
                    + " should have caused an exception, not given result " 
                    + badStatus.name();
            fail(msg);
        } catch (NoSuchElementException nsee) {
            System.out.println("Trying to query " + outOfBounds.toString() 
                    + ", which is beyond " + corner.toString() 
                    + " correctly caused NoSuchElementException");
            System.out.println("\"" + nsee.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to query " 
                    + outOfBounds.toString() + ", which is beyond " 
                    + corner.toString();
            fail(msg);
        }
    }

    /**
     * Another test of query function, of class Board.
     */
    @Test
    public void testMayQueryAfterWinning() {
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(POSITION_ZERO);
        Board board = new Board(POSITION_ZERO, mineLocs);
        board.flag(POSITION_ZERO);
        try {
            PositionStatus status = board.query(POSITION_ZERO);
            assertEquals(PositionStatus.FLAGGED, status);
        } catch (RuntimeException re) {
            String msg = "Querying after winning should not have caused " 
                    + re.getClass().getName();
            fail(msg);
        }
    }
    
    /**
     * Another test of query function, of class Board.
     */
    @Test
    public void testQueryShowsWrongFlagAfterLosing() {
        Position mineLoc1 = POSITION_ZERO;
        Position mineLoc2 = mineLoc1.nextRow();
        Position notAMineLoc = mineLoc1.nextColumn();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc1);
        mineLocs.add(mineLoc2);
        Position maxPos = mineLoc2.nextColumn();
        Board board = new Board(maxPos, mineLocs);
        board.flag(mineLoc1);
        board.flag(notAMineLoc);
        board.reveal(mineLoc2);
        String msg = "Detonating mine at " + mineLoc2.toString() 
                + " should have ended game in a loss";
        assert !board.gameUnderway() : msg;
        assert !board.gameWon() : msg;
        assertEquals(PositionStatus.FLAGGED, board.query(mineLoc1));
        assertEquals(PositionStatus.DETONATED, board.query(mineLoc2));
        assertEquals(PositionStatus.WRONGLY_FLAGGED, board.query(notAMineLoc));
    }

    /**
     * Another test of query function, of class Board.
     */
    @Test
    public void testQueryShowsUndetonatedUnflaggedMineAfterLosing() {
        Position mineLoc1 = POSITION_ZERO;
        Position mineLoc2 = mineLoc1.nextRow();
        Position mineLoc3 = mineLoc2.nextRow();
        Position notAMineLoc = mineLoc3.nextColumn();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc1);
        mineLocs.add(mineLoc2);
        mineLocs.add(mineLoc3);
        Position maxPos = notAMineLoc.nextColumn();
        Board board = new Board(maxPos, mineLocs);
        board.flag(mineLoc1);
        board.flag(notAMineLoc);
        board.reveal(mineLoc2);
        String msg = "Detonating mine at " + mineLoc2.toString() 
                + " should have ended game in a loss";
        assert !board.gameUnderway() : msg;
        assert !board.gameWon() : msg;
        assertEquals(PositionStatus.FLAGGED, board.query(mineLoc1));
        assertEquals(PositionStatus.DETONATED, board.query(mineLoc2));
        assertEquals(PositionStatus.REVEALED_MINED, board.query(mineLoc3));
    }

    /**
     * Test of reveal function, of class Board.
     */
    @Test
    public void testReveal() {
        System.out.println("reveal");
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(POSITION_ZERO);
        Board board = new Board(POSITION_ZERO, mineLocs);
        Optional<Mine> option = board.reveal(POSITION_ZERO);
        if (option.isPresent()) {
            assertEquals(POSITION_ZERO, option.get().getPosition());
        } else {
            fail("reveal() function should have revealed a mine");
        }
    }

    /**
     * Another test of reveal function, of class Board.
     */
    @Test
    public void testRevealEmptySwath() {
        int minedColumnEndX = 28;
        int minedColumnY = (int) Math.floor(Math.random() * 50) + 14;
        Position minedColumnStart = new Position(0, minedColumnY);
        Position minedColumnEnd = new Position(minedColumnEndX, minedColumnY);
        HashSet<Position> mineLocs = new HashSet<>();
        for (Position loc = minedColumnStart;
                loc.isWithinBounds(minedColumnEnd);
                loc = loc.nextRow()) {
            mineLocs.add(loc);
        }
        Board board = new Board(minedColumnEnd, mineLocs);
        board.reveal(POSITION_ZERO);
        Position presumedEmpty;
        String msg;
        for (int x = 0; x <= minedColumnEndX; x++) {
            for (int y = 0; y < minedColumnY - 1; y++) {
                presumedEmpty = new Position(x, y);
                msg = "Position " + presumedEmpty.toString() 
                        + " should be empty and without neighbors";
                assertEquals(msg, PositionStatus.REVEALED_EMPTY, 
                        board.query(presumedEmpty));
            }
            presumedEmpty = new Position(x, minedColumnY - 1);
            msg = "Position " + presumedEmpty.toString() 
                    + " should be empty but neighbor at least two mines";
            char neighborCount = board.query(presumedEmpty).getChar();
            assert neighborCount > '1' && neighborCount < '4' : msg;
        }
    }

    /**
     * Another test of reveal function, of class Board.
     */
    @Test
    public void testNoRevealForAlreadyRevealed() {
        Position mineLoc = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc);
        Position mineNeighbor = mineLoc.nextRow();
        Board board = new Board(mineNeighbor.nextColumn().nextRow(), mineLocs);
        Optional<Mine> option = board.reveal(mineNeighbor);
        String msg = "Position " + mineNeighbor + " should be empty";
        assert !option.isPresent() : msg;
        assertEquals(PositionStatus.REVEALED_EMPTY_NEAR_1, 
                board.query(mineNeighbor));
        try {
            option = board.reveal(mineNeighbor);
            msg = "Position " + mineNeighbor.toString() 
                    + " should not have been revealed a second time as " 
                    + option.toString();
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to reveal " + mineNeighbor.toString() 
                    + " twice correctly caused IllegalStateException");
            System.out.println("\"" + ise.getMessage() + "\"");
        } catch (RuntimeException re) {
            msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to reveal " 
                    + mineNeighbor.toString() + " twice";
            fail(msg);
        }
    }

    /**
     * Another test of reveal function, of class Board. Trying to reveal out of 
     * bounds positions should cause <code>NoSuchElementException</code>.
     */
    @Test
    public void testNoRevealOutOfBounds() {
        Position nextToCorner = PositionTest.makePosition();
        Position corner = nextToCorner.nextColumn();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(nextToCorner);
        mineLocs.add(corner);
        Board board = new Board(corner, mineLocs);
        Position outOfBounds = corner.nextColumn().nextRow();
        try {
            Optional<Mine> badOption = board.reveal(outOfBounds);
            String msg = "Trying to reveal position " + outOfBounds.toString() 
                    + ", which is beyond corner " + corner.toString() 
                    + " should have caused an exception, not given result " 
                    + badOption.toString();
            fail(msg);
        } catch (NoSuchElementException nsee) {
            System.out.println("Trying to reveal " + outOfBounds.toString() 
                    + ", which is beyond " + corner.toString() 
                    + " correctly caused NoSuchElementException");
            System.out.println("\"" + nsee.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to reveal " 
                    + outOfBounds.toString() + ", which is beyond " 
                    + corner.toString();
            fail(msg);
        }
    }

    /**
     * Another test of reveal function, of class Board.
     */
    @Test
    public void testNoRevealAfterGameOver() {
        Position mineLoc = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc);
        Position mineNeighbor = mineLoc.nextRow();
        Board board = new Board(mineNeighbor.nextColumn().nextRow(), mineLocs);
        Optional<Mine> option = board.reveal(mineLoc);
        if (option.isPresent()) {
            assertEquals(mineLoc, option.get().getPosition());
        } else {
            fail("reveal() function should have revealed a mine");
        }
        try {
            option = board.reveal(mineNeighbor);
            String msg = "Position " + mineNeighbor.toString() 
                    + " should not have been revealed as " + option.toString() 
                    + " after game over";
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to reveal " + mineNeighbor.toString() 
                    + " after game over correctly caused IllegalStateException");
            String excMsg = ise.getMessage();
            System.out.println("\"" + excMsg + "\"");
            String msg 
                    = "Exception message should contain the words \"Game Over\"";
            assert excMsg.toLowerCase().replace(" ", "").contains("gameover") 
                    : msg;
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to reveal " 
                    + mineNeighbor.toString() + " after game over";
            fail(msg);
        }
    }

    /**
     * Test of flag procedure, of class Board.
     */
    @Test
    public void testFlag() {
        System.out.println("flag");
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(POSITION_ZERO);
        Board board = new Board(POSITION_ZERO, mineLocs);
        board.flag(POSITION_ZERO);
        assertEquals(PositionStatus.FLAGGED, board.query(POSITION_ZERO));
    }
    
    /**
     * Test of unflag procedure, of class Board.
     */
    @Test
    public void testUnflag() {
        System.out.println("unflag");
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(POSITION_ZERO);
        mineLocs.add(POSITION_ZERO.nextColumn());
        Board board = new Board(POSITION_ZERO.nextColumn().nextRow(), mineLocs);
        board.flag(POSITION_ZERO);
        assertEquals(PositionStatus.FLAGGED, board.query(POSITION_ZERO));
        board.unflag(POSITION_ZERO);
        assertEquals(PositionStatus.COVERED, board.query(POSITION_ZERO));
    }

    /**
     * Another test of flag procedure, of class Board.
     */
    @Test
    public void testNoFlagForAlreadyFlagged() {
        Position mineLoc = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc);
        Board board = new Board(mineLoc.nextColumn(), mineLocs);
        board.flag(mineLoc);
        assertEquals(PositionStatus.FLAGGED, board.query(mineLoc));
        try {
            board.flag(mineLoc);
            String msg = "Should not have been able to flag " 
                    + mineLoc.toString() + " again without first unflagging";
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to flag " + mineLoc.toString() 
                    + " twice correctly caused IllegalStateException");
            System.out.println("\"" + ise.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to flag " 
                    + mineLoc.toString() + " twice";
            fail(msg);
        }
    }
    
    /**
     * Another test of flag procedure, of class Board. Trying to flag out of 
     * bounds positions should cause <code>NoSuchElementException</code>.
     */
    @Test
    public void testNoFlagOutOfBounds() {
        Position nextToCorner = PositionTest.makePosition();
        Position corner = nextToCorner.nextColumn();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(nextToCorner);
        mineLocs.add(corner);
        Board board = new Board(corner, mineLocs);
        Position outOfBounds = corner.nextColumn().nextRow();
        try {
            board.flag(outOfBounds);
            String msg = "Should not have been able to flag " 
                    + outOfBounds.toString() + ", which is beyond corner " 
                    + corner.toString();
            fail(msg);
        } catch (NoSuchElementException nsee) {
            System.out.println("Trying to flag " + outOfBounds.toString() 
                    + ", which is beyond " + corner.toString() 
                    + " correctly caused NoSuchElementException");
            System.out.println("\"" + nsee.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to flag " 
                    + outOfBounds.toString() + ", which is beyond " 
                    + corner.toString();
            fail(msg);
        }
    }

    /**
     * Another test of flag procedure, of class Board.
     */
    @Test
    public void testNoFlagAfterGameOver() {
        Position mineLoc = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc);
        Board board = new Board(mineLoc.nextColumn(), mineLocs);
        Optional<Mine> option = board.reveal(mineLoc);
        String msg = "Position " + mineLoc.toString() + " should have a mine";
        assert option.isPresent() : msg;
        msg = "Detonating mine at " + mineLoc.toString() 
                + " should be game over";
        assert !board.gameUnderway() : msg;
        assertEquals(PositionStatus.DETONATED, board.query(mineLoc));
        try {
            board.flag(mineLoc);
            msg = "Should not have been able to flag position " 
                    + mineLoc.toString() + " after detonating its mine";
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to flag " + mineLoc.toString() 
                    + " after detonation correctly caused IllegalStateException");
            String excMsg = ise.getMessage();
            System.out.println("\"" + excMsg + "\"");
            msg = "Exception message should contain the words \"Game Over\"";
            assert excMsg.toLowerCase().replace(" ", "").contains("gameover") 
                    : msg;
        } catch (RuntimeException re) {
            msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to flag " 
                    + mineLoc.toString() + " after detonation";
            fail(msg);
        }
    }

    /**
     * Another test of unflag procedure, of class Board.
     */
    @Test
    public void testNoUnflagIfNotFlagged() {
        Position mineLoc = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc);
        Board board = new Board(mineLoc.nextColumn(), mineLocs);
        assertEquals(PositionStatus.COVERED, board.query(mineLoc));
        try {
            board.unflag(mineLoc);
            String msg = "Should not have been able to unflag " 
                    + mineLoc.toString() + " again without first flagging";
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to unflag " + mineLoc.toString() 
                    + " twice correctly caused IllegalStateException");
            System.out.println("\"" + ise.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to unflag " 
                    + mineLoc.toString() + " twice";
            fail(msg);
        }
    }

    /**
     * Another test of unflag procedure, of class Board. Trying to unflag out of 
     * bounds positions should cause <code>NoSuchElementException</code>.
     */
    @Test
    public void testNoUnflagOutOfBounds() {
        Position nextToCorner = PositionTest.makePosition();
        Position corner = nextToCorner.nextColumn();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(nextToCorner);
        mineLocs.add(corner);
        Board board = new Board(corner, mineLocs);
        Position outOfBounds = corner.nextColumn().nextRow();
        try {
            board.unflag(outOfBounds);
            String msg = "Should not have been able to unflag " 
                    + outOfBounds.toString() + ", which is beyond corner " 
                    + corner.toString();
            fail(msg);
        } catch (NoSuchElementException nsee) {
            System.out.println("Trying to unflag " + outOfBounds.toString() 
                    + ", which is beyond " + corner.toString() 
                    + " correctly caused NoSuchElementException");
            System.out.println("\"" + nsee.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to unflag " 
                    + outOfBounds.toString() + ", which is beyond " 
                    + corner.toString();
            fail(msg);
        }
    }

    /**
     * Another test of unflag procedure, of class Board.
     */
    @Test
    public void testNoUnflagAfterGameOver() {
        Position mineLoc = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(mineLoc);
        Board board = new Board(mineLoc.nextColumn().nextRow(), mineLocs);
        Position mineNeighbor = mineLoc.nextColumn();
        board.flag(mineNeighbor);
        Optional<Mine> option = board.reveal(mineLoc);
        String msg = "Position " + mineLoc.toString() + " should have a mine";
        assert option.isPresent() : msg;
        msg = "Detonating mine at " + mineLoc.toString() 
                + " should be game over";
        assert !board.gameUnderway() : msg;
        assertEquals(PositionStatus.DETONATED, board.query(mineLoc));
        try {
            board.unflag(mineNeighbor);
            msg = "Should not have been able to unflag position " 
                    + mineNeighbor.toString() 
                    + " after detonating neighboring mine";
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to unflag " + mineNeighbor.toString() 
                    + " after game over correctly caused IllegalStateException");
            String excMsg = ise.getMessage();
            System.out.println("\"" + excMsg + "\"");
            msg = "Exception message should contain the words \"Game Over\"";
            assert excMsg.toLowerCase().replace(" ", "").contains("gameover") 
                    : msg;
        } catch (RuntimeException re) {
            msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to flag " 
                    + mineLoc.toString() + " after detonating its neighbor";
            fail(msg);
        }
    }

    /**
     * Test of makeBoard function, of class Board.
     */
    @Test
    public void testMakeBoard() {
        System.out.println("makeBoard");
        Position maxPos = PositionTest.makePosition();
        int numberOfMines = maxPos.getX() * maxPos.getY();
        Board board = Board.makeBoard(numberOfMines, maxPos);
        Optional<Mine> option;
        Position currLoc = POSITION_ZERO;
        PositionStatus status;
        String msg;
        while (currLoc.isWithinBounds(maxPos) && board.gameUnderway()) {
            status = board.query(currLoc);
            if (status.equals(PositionStatus.COVERED)) {
                option = board.reveal(currLoc);
                if (option.isPresent()) {
                    msg = "Oops, stepped on mine at " + currLoc.toString();
                    assert !board.gameUnderway() : msg;
                }
            }
            currLoc = currLoc.nextColumnWithReset(maxPos);
        }
        msg = "Board should have had a mine that detonated and ended the game";
        assert !board.gameUnderway() : msg;
    }
    
    /**
     * Another test of makeBoard function, of class Board.
     */
    @Test
    public void testMakeBoardRejectsNegativeNumberOfMines() {
        int negativeNumber = (int) Math.floor(Math.random() * (-20)) - 5;
        Position maxPosition = PositionTest.makePosition();
        try {
            Board badBoard = Board.makeBoard(negativeNumber, maxPosition);
            String msg = "Should not have been able to create board " 
                    + badBoard.toString() + " with " + negativeNumber + " mines";
            fail(msg);
        } catch (IllegalArgumentException iae) {
            System.out.println(negativeNumber
                    + " mines correctly caused IllegalArgumentException");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for " + negativeNumber 
                    + " mines";
            fail(msg);
        }
    }
    
    /**
     * Another test of makeBoard function, of class Board.
     */
    @Test
    public void testMakeBoardRejectsExcessiveNumberOfMines() {
        int sideLength = (int) Math.floor(Math.random() * 20) + 5;
        int numberOfMines = sideLength * sideLength + 1;
        Position maxPosition = new Position(sideLength - 1, sideLength - 1);
        try {
            Board badBoard = Board.makeBoard(numberOfMines, maxPosition);
            String msg = "Should not have been able to create board " 
                    + badBoard.toString() + " that is " + sideLength + " by " 
                    + sideLength + " with " + numberOfMines + " mines";
            fail(msg);
        } catch (IllegalArgumentException iae) {
            System.out.println("Trying to make " + sideLength + " by " 
                    + sideLength + " board with " + numberOfMines 
                    + " mines correctly caused IllegalArgumentException");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for trying to create "
                    + sideLength + " by " + sideLength + " board with " 
                    + numberOfMines + " mines";
            fail(msg);
        }
    }
    
    /**
     * Test of gameUnderway function, of class Board.
     */
    @Test
    public void testGameUnderway() {
        System.out.println("gameUnderway");
        Position maxPos = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        Board board = new Board(maxPos, mineLocs);
        String msg = "Game should be considered to be underway";
        assert board.gameUnderway() : msg;
    }
    
    /**
     * Another test of gameUnderway function, of class Board.
     */
    @Test
    public void testGameOver() {
        Position maxPos = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(POSITION_ZERO);
        Board board = new Board(maxPos, mineLocs);
        board.reveal(POSITION_ZERO);
        String msg = "Game should be considered over after detonating mine";
        assert !board.gameUnderway() : msg;
    }
    
    /**
     * Test of gameWon function, of class Board.
     */
    @Test
    public void testGameWon() {
        System.out.println("gameWon");
        Position maxPos = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        Position mineLoc = POSITION_ZERO;
        while (mineLoc.isWithinBounds(maxPos)) {
            mineLocs.add(mineLoc);
            mineLoc = mineLoc.nextRow();
        }
        Board board = new Board(maxPos, mineLocs);
        mineLoc = POSITION_ZERO;
        while (mineLoc.isWithinBounds(maxPos)) {
            board.flag(mineLoc);
            mineLoc = mineLoc.nextRow();
        }
        String msg = "With all mines from " + POSITION_ZERO.toString() 
                + " stopping short of " + mineLoc.toString() 
                + " flagged, the game should be considered won";
        assert board.gameWon() : msg;
        assert !board.gameUnderway() : msg;
    }
    
    /**
     * Another test of gameWon function, of class Board.
     */
    @Test
    public void testGameLost() {
        Position maxPos = PositionTest.makePosition();
        HashSet<Position> mineLocs = new HashSet<>();
        Position mineLoc = POSITION_ZERO;
        while (mineLoc.isWithinBounds(maxPos)) {
            mineLocs.add(mineLoc);
            mineLoc = mineLoc.nextRow();
        }
        Board board = new Board(maxPos, mineLocs);
        Optional<Mine> option = board.reveal(POSITION_ZERO);
        String msg = "Option for position " + POSITION_ZERO.toString() 
                + " should contain a detonated mine";
        assert option.isPresent() : msg;
        assert option.get().hasBeenDetonated() : msg;
        msg = "Detonated mine means game over and lost";
        assert !board.gameUnderway() : msg;
        assert !board.gameWon() : msg;
    }
    
    /**
     * Another test of gameWon function, of class Board. The player should not 
     * be able to win simply by flagging every position. To win, the player must 
     * only flag mined positions and not flag empty positions. However, the 
     * player may win even after flagging an incorrect position by unflagging 
     * it, if all the remaining flags are on all the correct positions.
     */
    @Test
    public void testCanStillWinAfterBadFlag() {
        int maxColumn = (int) Math.floor(Math.random() * 10) + 8;
        Position maxPos = new Position(1, maxColumn);
        HashSet<Position> mineLocs = new HashSet<>();
        Position mineLoc = new Position(1, 0);
        while (mineLoc.isWithinBounds(maxPos)) {
            mineLocs.add(mineLoc);
            mineLoc = mineLoc.nextColumn();
        }
        Board board = new Board(maxPos, mineLocs);
        Position wrongMineLoc = new Position(0, maxColumn - 3);
        board.flag(wrongMineLoc);
        mineLoc = wrongMineLoc.nextRowColumnZero();
        while (mineLoc.isWithinBounds(maxPos)) {
            board.flag(mineLoc);
            mineLoc = mineLoc.nextColumn();
        }
        String msg = "Because of wrong flag on " + wrongMineLoc.toString() 
                + ", the game should still be ongoing";
        assert board.gameUnderway() : msg;
        board.unflag(wrongMineLoc);
        msg = "Now that wrong location " + wrongMineLoc.toString() 
                + " has been unflagged, game should be over and won";
        assert !board.gameUnderway() : msg;
        assert board.gameWon() : msg;
    }
    
    /**
     * Test of the constructor.
     */
    @Test
    public void testConstructorRejectsOutOfBoundsMines() {
        System.out.println("Constructor");
        Position maxPos = PositionTest.makePosition();
        Position badMineLoc = maxPos.nextColumn().nextRow();
        HashSet<Position> mineLocs = new HashSet<>();
        mineLocs.add(badMineLoc);
        try {
            Board badBoard = new Board(maxPos, mineLocs);
            String msg = "Should not have been able to create board " 
                    + badBoard.toString() + " with mine at position " 
                    + badMineLoc.toString() 
                    + ", which is beyond supposed maximum position " 
                    + maxPos.toString();
            fail(msg);
        } catch (IllegalArgumentException iae) {
            System.out.println("Trying to place mine at " 
                    + badMineLoc.toString() 
                    + ", which is beyond supposed maximum position " 
                    + maxPos.toString() 
                    + " correctly caused IllegalArgumentException");
            System.out.println("\"" + iae.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception to throw for mine location " 
                    + badMineLoc.toString() 
                    + ", which is beyond supposed maximum position " 
                    + maxPos.toString();
            fail(msg);
        }
    }
    
}
