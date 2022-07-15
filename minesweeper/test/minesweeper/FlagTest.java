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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the Flag class.
 * @author Alonso del Arte
 */
public class FlagTest {
    
    /**
     * Test of getPosition method, of class Flag.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        Position expected = PositionTest.makePosition();
        Flag flag = new Flag(expected, true);
        Position actual = flag.getPosition();
        assertEquals(expected, actual);
    }

    /**
     * Test of isCorrect method, of class Flag.
     */
    @Test
    public void testIsCorrect() {
        System.out.println("isCorrect");
        Position position = PositionTest.makePosition();
        Flag flag = new Flag(position, true);
        String msg = "Flag placed at " + position.toString() 
                + " should be correct";
        assert flag.isCorrect() : msg;
    }
    
    /**
     * Another test of isCorrect method, of class Flag.
     */
    @Test
    public void testIsNotCorrect() {
        Position position = PositionTest.makePosition();
        Flag flag = new Flag(position, false);
        String msg = "Flag placed at " + position.toString() 
                + " should not be correct";
        assert !flag.isCorrect() : msg;
    }
    
}
