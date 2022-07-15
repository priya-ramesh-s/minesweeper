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
 * Tests of the Mine class.
 * @author Alonso del Arte
 */
public class MineTest {
    
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        Position expected = PositionTest.makePosition();
        Mine mine = new Mine(expected);
        Position actual = mine.getPosition();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHasBeenDetonated() {
        System.out.println("hasBeenDetonated");
        Position location = PositionTest.makePosition();
        Mine mine = new Mine(location);
        String msg = "New mine at " + location.toString() 
                + " should not already be detonated";
        assert !mine.hasBeenDetonated() : msg;
    }
    
    @Test
    public void testDetonate() {
        System.out.println("detonate");
        Position location = PositionTest.makePosition();
        Mine mine = new Mine(location);
        mine.detonate();
        String msg = "Detonated mine at " + location.toString() 
                + " should be reported as detonated";
        assert mine.hasBeenDetonated() : msg;
    }
    
    @Test
    public void testOneMineShouldNotDetonateTwice() {
        Position location = PositionTest.makePosition();
        Mine mine = new Mine(location);
        mine.detonate();
        try {
            mine.detonate();
            String msg = "Should not have been able to detonate mine at " 
                    + location.toString() + " twice";
            fail(msg);
        } catch (IllegalStateException ise) {
            System.out.println("Trying to detonate mine at " 
                    + location.toString() 
                    + " twice correctly caused IllegalStateException");
            System.out.println("\"" + ise.getMessage() + "\"");
        } catch (RuntimeException re) {
            String msg = re.getClass().getName() 
                    + " is the wrong exception for trying to detonate mine at " 
                    + location.toString() + " twice";
            fail(msg);
        }
    }
    
}
