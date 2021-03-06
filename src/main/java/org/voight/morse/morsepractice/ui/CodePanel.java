/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.voight.morse.morsepractice.ui;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JPanel;
import org.voight.morse.morsepractice.Symbol;

/**
 *
 * @author Jeffrey Voight <jeff.voight@gmail.com>
 */
public class CodePanel extends JPanel {

    Symbol symbol;
    boolean clear = false;
    int charWidth, charHeight, width, height, centerWidth,
            centerHeight, dashHeight, dashWidth, dotHeight, dotWidth, symbolLength;
    String symbolString;

    public CodePanel() {
        super();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                charWidth = getWidth() / 5;
                charHeight = getHeight();
                centerWidth = charWidth / 2;
                centerHeight = charHeight / 2;
                dashHeight = (int) (charHeight * .2); // 20%
                dashWidth = (int) (charWidth * .8); // 80%
                dotHeight = (int) (charHeight * .2); // 20%
                dotWidth = (int) (charWidth * .2); // 80%
            }
        });

        try {
            symbol = new Symbol(' ', "", 700, 10); // This is a dummy symbol. Probably never get seen
        } catch (LineUnavailableException ex) {
            Logger.getLogger(CodePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, width, height);
        if (!clear && symbol != null) {            
            for (int i = 0; i < symbolLength; i++) {
                char theChar = symbolString.charAt(i);
                if ('1' == theChar) {
                    drawDash(g, i * charWidth);
                } else {
                    drawDot(g, i * charWidth);
                }
            }
        }
    }

    public void clearSymbol() {
        clear = true;
        this.paintImmediately(0, 0, width, height); // These two lines are buggy

    }

    public void setSymbol(Symbol s) {
        clear = false;
        symbol = s;
        symbolString = symbol.getCode();
        symbolLength = symbolString.length();
        this.repaint(20);
    }

    private void drawDash(Graphics g, int start) {
        int x = start + centerWidth - dashWidth / 2;
        int y = centerHeight - dashHeight / 2;
        g.fillRoundRect(x, y, dashWidth, dashHeight, 9, 9);
    }

    private void drawDot(Graphics g, int start) {        
        int x = start + centerWidth - dotWidth / 2;
        int y = centerHeight - dotHeight / 2;
        g.fillOval(x, y, dotWidth, dotHeight);

    }
}
