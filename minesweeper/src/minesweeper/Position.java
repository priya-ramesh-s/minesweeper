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

import java.util.HashSet;
import java.util.Random;

/**
 * Represents a position on the board. An instance of this class only indicates 
 * a position on the board, it does not indicate whether the position has a mine 
 * nor whether the position has been flagged. Positions with negative 
 * coordinates are not allowed. This class is immutable.
 * @author Alonso del Arte
 */
public class Position {
    
    private static final Random RANDOM = new Random();
    
    private final int coordX, coordY;
    
    /**
     * Retrieves the <i>x</i> coordinate. There is no corresponding setter, this 
     * class is immutable.
     * @return The <i>x</i> coordinate. For example, for (47, 20), this function 
     * will return 47.
     */
    public int getX() {
        return this.coordX;
    }
    
    /**
     * Retrieves the <i>y</i> coordinate. There is no corresponding setter, this 
     * class is immutable.
     * @return The <i>y</i> coordinate. For example, for (47, 20), this function 
     * will return 20.
     */
    public int getY() {
        return this.coordY;
    }
    
    /**
     * A textual representation, consisting of the <i>x</i> and <i>y</i> 
     * coordinates. For the <i>x</i> and <i>y</i> coordinates as numbers, use 
     * the getters {@link #getX()} and {@link #getY()}.
     * @return A <code>String</code> of the form "(<i>x</i>, <i>y</i>)". For 
     * example, if <i>x</i> = 47 and <i>y</i> = 20, this function will return 
     * "(47, 20)".
     */
    @Override
    public String toString() {
        return "(" + this.coordX + ", " + this.coordY + ")";
    }

    /**
     * Determines whether this <code>Position</code> instance is equal to a 
     * given object.
     * @param obj The object to check for equality. May be null. Examples: the 
     * <code>Position</code> instances (47, 19), (46, 20), (47, 20); a 
     * <code>Mine</code> at (47, 20); the <code>java.awt.Point</code> (47, 20).
     * @return True if <code>obj</code> is a <code>Position</code> instance with 
     * the same <i>x</i> and <i>y</i> coordinates, false in any other case. For 
     * the examples, let's say this <code>Position</code> instance is (47, 20). 
     * Then if <code>obj</code> is (47, 19), this function would return false 
     * even though <i>x</i> matches; also false for (46, 20) even though 
     * <i>y</i> matches; true for (47, 20); false for null; false for the 
     * <code>Mine</code> at (47, 20); and false for the 
     * <code>java.awt.Point</code> (47, 20), even though their <i>x</i> and 
     * <i>y</i> coordinates match.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!this.getClass().equals(obj.getClass())) return false;
        Position other = (Position) obj;
        return (this.coordX == other.coordX) 
                && (this.coordY == other.coordY);
    }

    /**
     * Returns a hash code for this position. The hash code is based on the 
     * <i>x</i> and <i>y</i> coordinates only, and is guaranteed to be unique if 
     * neither the <i>x</i> nor <i>y</i> coordinates of any 
     * <code>Position</code> instance at runtime exceeds 65535.
     * @return A hash code. For example, for (47, 20), this might be 3080212; 
     * for (20, 47), this might be 1310767. Guaranteed to be positive if 
     * <i>x</i> &lt; 32768.
     */
    @Override
    public int hashCode() {
        return (this.coordX << 16) + (this.coordY & 65535);
    }
    
    /**
     * Determines whether this position is within given rectangular boundaries. 
     * The top left corner is implicitly (0, 0).
     * @param boundingCorner The corner with the maximum allowable <i>x</i> and 
     * <i>y</i> coordinates for this position to be considered within bounds. 
     * For example, (10, 10).
     * @return True if this position's <i>x</i> and <i>y</i> coordinates are 
     * both no greater than the <i>x</i> and <i>y</i> coordinates of 
     * <code>boundingCorner</code>. Given the example 
     * <code>boundingCorner</code>, (10, 9) and (9, 10), as well as (10, 10), 
     * would all be considered to be within bounds. (11, 10) and (10, 11) would 
     * not, and definitely not (11, 11).
     */
    public boolean isWithinBounds(Position boundingCorner) {
        return !this.isOutsideBounds(boundingCorner);
    }
    
    private boolean isOutsideBounds(Position boundingCorner) {
        return (this.coordX > boundingCorner.coordX 
                || this.coordY > boundingCorner.coordY);
    }
    
    /**
     * Retrieves a set of immediately neighboring positions, excluding this 
     * position itself, and also excluding positions outside the given boundary. 
     * The top left corner is implicitly (0, 0); positions with negative 
     * <i>x</i> and/or <i>y</i> coordinates are ignored. To be considered 
     * "immediately neighboring," the positions must share either a side or a 
     * corner. In other words, the neighbor's <i>x</i> coordinate is this 
     * position's <i>x</i> &plusmn; 1 or 0, and the neighbor's <i>y</i> 
     * coordinate is this position's <i>y</i> &plusmn; 1 or 0, but the offset 
     * can't be 0 for both.
     * @param boundingCorner The corner with the maximum allowable <i>x</i> and 
     * <i>y</i> coordinates for a neighboring position to be considered within 
     * bounds. For example, (10, 10).
     * @return A set of the immediately neighboring positions. For example, for 
     * (10, 0), this would be (9, 0), (9, 1) and (10, 1). The positions  (11, 0) 
     * and (11, 1) would be omitted on account of <code>boundingCorner</code> 
     * being (10, 10).
     */
    public HashSet<Position> getNeighbors(Position boundingCorner) {
        HashSet<Position> set = this.getNeighbors();
        set.removeIf(pos -> pos.isOutsideBounds(boundingCorner));
        return set;
    }
    
    /**
     * Retrieves a set of immediately neighboring positions, excluding this 
     * position itself. The top left corner is implicitly (0, 0); positions with 
     * negative <i>x</i> and/or <i>y</i> coordinates are ignored. To be 
     * considered "immediately neighboring," the positions must share either a 
     * side or a corner. In other words, the neighbor's <i>x</i> coordinate is 
     * this position's <i>x</i> &plusmn; 1 or 0, and the neighbor's <i>y</i> 
     * coordinate is this position's <i>y</i> &plusmn; 1 or 0, but the offset 
     * can't be 0 for both.
     * @return A set of the immediately neighboring positions. For example, for 
     * (10, 0), this would be (9, 0), (9, 1), (10, 1), (11, 0) and (11, 1).
     */
    public HashSet<Position> getNeighbors() {
        HashSet<Position> set = new HashSet<>();
        int startX = this.coordX - 1;
        int startY = this.coordY - 1;
        int finishX = this.coordX + 2;
        int finishY = this.coordY + 2;
        if (startX == -1) startX = 0;
        if (startY == -1) startY = 0;
        Position curr;
        for (int x = startX; x < finishX; x++) {
            for (int y = startY; y < finishY; y++) {
                curr = new Position(x, y);
                set.add(curr);
            }
        }
        set.remove(this);
        return set;
    }
    
    /**
     * Advances to the next column. May be used to loop through positions.
     * @return A new <code>Position</code> instance with the same coordinates, 
     * except with <i>y</i> on the next column.
     * @throws IllegalArgumentException If this position's <i>y</i> coordinate 
     * is <code>Integer.MAX_VALUE</code>.
     */
    public Position nextColumn() {
        return new Position(this.coordX, this.coordY + 1);
    }
    
    /**
     * Advances to the next column, or to column 0 on the next row if the 
     * bounding column has been reached. May be used to loop through positions.
     * @param boundingCorner A position with the bounding column.
     * @return A new <code>Position</code> instance with the same coordinates, 
     * except with <i>y</i> on the next column.
     * @throws IllegalArgumentException If this position's <i>y</i> coordinate 
     * is <code>Integer.MAX_VALUE</code>.
     */
    public Position nextColumnWithReset(Position boundingCorner) {
        int x = this.coordX;
        int y = this.coordY + 1;
        if (y > boundingCorner.coordY) {
            x++;
            y = 0;
        }
        return new Position(x, y);
    }
    
    /**
     * Advances to the next row. May be used to loop through positions.
     * @return A new <code>Position</code> instance with the same coordinates, 
     * except with <i>x</i> on the next row.
     * @throws IllegalArgumentException If this position's <i>x</i> coordinate 
     * is <code>Integer.MAX_VALUE</code>.
     */
    public Position nextRow() {
        return new Position(this.coordX + 1, this.coordY);
    }
    
    /**
     * Advances to column 0 on the next row. May be used to loop through 
     * positions.
     * @return A new <code>Position</code> instance, with <i>x</i> + 1 for 
     * <i>x</i> and <i>y</i> = 0.
     * @throws IllegalArgumentException If this position's <i>x</i> coordinate 
     * is <code>Integer.MAX_VALUE</code>.
     */
    public Position nextRowColumnZero() {
        return new Position(this.coordX + 1, 0);
    }
    
    /**
     * Chooses a position pseudorandomly. It should be random enough for most 
     * games.
     * @param boundingCorner A <code>Position</code> with the maximum allowable 
     * <i>x</i> and <i>y</i> for the randomly chosen <code>Position</code>. This 
     * bounding parameter is inclusive, unlike upper bounding parameters in the 
     * JDK, which tend to be exclusive.
     * @return A <code>Position</code> with <i>x</i> at least 0 and at most 
     * <code>boundingCorner</code>'s <i>x</i>, and <i>y</i> at least 0 and at 
     * most <code>boundingCorner</code>'s <i>y</i>. This <code>Position</code> 
     * could be <code>boundingCorner</code> itself, to ensure the possibility 
     * that the bottom right corner of a Minesweeper board could be mined.
     * @throws IllegalArgumentException If either <code>boundingCorner</code>'s 
     * <i>x</i> or <i>y</i> coordinate is <code>Integer.MAX_VALUE</code>. This 
     * means that (2147483646, 2147483646) is effectively the maximum allowable 
     * <code>boundingCorner</code> argument for this function. But since I 
     * expect Minesweeper boards to be much smaller than that, this limitation 
     * does not worry me in the slightest.
     */
    public static Position random(Position boundingCorner) {
        int x = RANDOM.nextInt(boundingCorner.coordX + 1);
        int y = RANDOM.nextInt(boundingCorner.coordY + 1);
        return new Position(x, y);
    }
    
    /**
     * Constructs a new instance. Sole constructor.
     * @param x An integer, at least 0 but no greater than 
     * <code>Integer.MAX_VALUE</code>. For best results, it should not be 
     * greater than 128.
     * @param y An integer, at least 0 but no greater than 
     * <code>Integer.MAX_VALUE</code>. For best results, it should not be 
     * greater than 128.
     * @throws IllegalArgumentException If either <code>x</code> or 
     * <code>y</code> is negative.
     */
    public Position(int x, int y) {
        if (x < 0 || y < 0) {
            String excMsg = "Coordinates with negative number(s) (" + x + ", " 
                    + y + ") are not allowed";
            throw new IllegalArgumentException(excMsg);
        }
        this.coordX = x;
        this.coordY = y;
    }
    
}
