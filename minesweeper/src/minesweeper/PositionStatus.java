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
 * Indicates the status of a position. A position should start out covered. As 
 * the game progresses, it may be revealed to be empty with a number of 
 * neighboring mines, or it may be revealed to be mined. Also, the position may 
 * be flagged, which may turn out to be wrongly flagged.
 * @author Alonso del Arte
 */
public enum PositionStatus {
    
    /**
     * Indicates the position has been revealed to not contain a mine. Depending 
     * on the user interface, the player might receive some indication as to the 
     * possible status of neighboring positions.
     */
    REVEALED_EMPTY (' ', "revealed empty"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors one mine. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_1 ('1', "revealed empty but neighboring one mine"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors two mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_2 ('2', "revealed empty but neighboring two mines"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors three mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_3 ('3', "revealed empty but neighboring three mines"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors four mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_4 ('4', "revealed empty but neighboring four mines"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors five mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_5 ('5', "revealed empty but neighboring five mines"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors six mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_6 ('6', "revealed empty but neighboring six mines"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors seven mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_7 ('7', "revealed empty but neighboring seven mines"),
    
    /**
     * Indicates the position has been revealed to not contain a mine, but it 
     * neighbors eight mines. Depending on the user interface, the player might 
     * receive some indication as to the possible status of neighboring 
     * positions.
     */
    REVEALED_EMPTY_NEAR_8 ('8', "revealed empty but neighboring eight mines"),
    
    /**
     * Indicates the position is covered. It may or may not have a mine. The 
     * player has not asserted anything about the position.
     */
    COVERED ('?', "covered"),
    
    /**
     * Indicates the position has a mine but the mine has not detonated. This 
     * status should only occur when the player has lost the game by detonating 
     * another mine in the field.
     */
    REVEALED_MINED ('x', "revealed mined"),
    
    /**
     * Indicates the position has been flagged because the player believes the 
     * position has a mine. Whether or not the position actually does have a 
     * mine, well, that's the fun of the game, isn't it?
     */
    FLAGGED ('!', "flagged"),
    
    /**
     * Indicates the position was flagged incorrectly. Because of this mistake, 
     * the player probably stepped on a mine in another position.
     */
    WRONGLY_FLAGGED ('w', "wrongly flagged"),
    
    /**
     * Indicates the position has a mine that detonated. The player has lost the 
     * game.
     */
    DETONATED ('X', "detonated");
    
    private final char symbol;
    
    private final String term;
    
    /**
     * Gets a character suitable for the command line version of this game.
     * @return An ASCII character, according to the status of the position:
     * <ul>
     * <li>'?' if the position is covered.</li>
     * <li>'!' if the position is flagged.</li>
     * <li>' ' (space) if the position has been revealed to be empty, and have 
     * no immediately neighboring mines.</li>
     * <li>A digit from '1' to '8' if the position has been revealed to be empty  
     * but it has a number of mined immediate neighbors.</li>
     * <li>'x' if the position has been revealed to have a mine that has not 
     * been detonated.</li>
     * <li>'X' if the position has a detonated mine.</li>
     * <li>'w' if the position was wrongly flagged (that is, the player flagged 
     * it but it doesn't actually have a mine).</li>
     * </ul>
     */
    public char getChar() {
        return this.symbol;
    }
    
    @Override
    public String toString() {
        return this.term;
    }

    private PositionStatus(char ch, String s) {
        this.symbol = ch;
        this.term = s;
    }
        
}
