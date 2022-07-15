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
package ui.graphical;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Alonso del Arte
 */
public class Game {
    
    // TODO: Implement the Minesweeper game as a Java Swing program.
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Minesweeper PLACEHOLDER");
        JPanel panel = new JPanel();
        String text = "Sorry, graphical UI is not ready yet";
        panel.add(new JLabel(text));
        panel.add(new JLabel(text));
        panel.add(new JLabel(text));
        Dimension dimension = new Dimension(360, 240);
        frame.setPreferredSize(dimension);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
}
