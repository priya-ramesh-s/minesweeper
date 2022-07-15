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

/**
 * Indicates that a position is believed to have a mine. This class is 
 * immutable. It is package private because outside of the package it would 
 * reveal whether or not the position has been flagged correctly.
 * @author Alonso del Arte
 */
class Flag {
    
    private final Position position;
    
    private final boolean correctness;
    
    /**
     * Retrieves the position of this flag.
     * @return A <code>Position</code> object. It should be the same one that 
     * was provided to the constructor.
     */
    public Position getPosition() {
        return this.position;
    }
    
    /**
     * Indicates whether this flag is a correct flag for its position. If it is, 
     * it brings the player one step closer to winning. But if it's not, it will 
     * probably lead the player to incorrectly believe that another position is 
     * safe when it is in fact mined.
     * @return True if the position is mined, false otherwise. This is the same 
     * Boolean value that was provided to the constructor.
     */
    boolean isCorrect() {
        return this.correctness;
    }
    
    /**
     * Sole constructor.
     * @param pos The position for the flag.
     * @param correct True if the position is mined, false otherwise.
     */
    Flag(Position pos, boolean correct) {
        this.position = pos;
        this.correctness = correct;
    }
    
}
