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
 * Represents a mine in the Minesweeper game. The class would be immutable 
 * except that it has one item of state: whether or not the mine has been 
 * detonated.
 * @author Alonso del Arte
 */
public class Mine {
    
    private final Position position;
    
    private boolean detonated = false;
    
    /**
     * Retrieves the position of this mine.
     * @return A <code>Position</code> object. It should be the same one that 
     * was provided to the constructor.
     */
    public Position getPosition() {
        return this.position;
    }
    
    /**
     * Tells whether or not the mine has been detonated.
     * @return True if the mine has been detonated, false if not.
     */
    public boolean hasBeenDetonated() {
        return this.detonated;
    }
    
    /**
     * Detonates the mine. Then {@link #hasBeenDetonated()} will return true.
     * @throws IllegalStateException If the mine has already been detonated.
     */
    public void detonate() {
        if (this.detonated) {
            String excMsg = "This mine has already been detonated";
            throw new IllegalStateException(excMsg);
        }
        this.detonated = true;
    }
    
    /**
     * Sole constructor.
     * @param pos Where to place the mine. Once placed, the mine can't be moved.
     */
    Mine(Position pos) {
        this.position = pos;
    }
    
}
