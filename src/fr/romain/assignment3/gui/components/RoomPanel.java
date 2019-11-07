package fr.romain.assignment3.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.JPanel;

public class RoomPanel extends JPanel {
    private final int width;
    private final int height;
    private final int nSquares;
    private final int[] factors;
    private final int dx, dy;
    private Color[] colors;

    public RoomPanel(int width, int height, int squares) {
        this.nSquares = squares;
        this.factors = getFactors(squares);
        this.width = width - factors[0] + 1; // - border space
        this.height = height - factors[1] + 1; // - border space
        this.dx = this.width / factors[0];
        this.dy = this.height / factors[1];

        colors = new Color[squares];
        Arrays.fill(colors, Color.GREEN);
    }

    public void setColor(int place, Color color) {
        colors[place] = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int nX = 0;
        int nY = 0;
        int n = 0;
        for (int i = 0; i < getHeight() && nY++ < factors[1]; i = i + dy + 1) {
            for (int j = 0; j < getWidth() && nX++ < factors[0]; j = j + dx + 1) {
                Color color = colors[n++];
                g2d.setColor(color);
                g2d.fillRect(j, i, dx, dy);
            }
            nX = 0;
        }
    }

    private int[] getFactors(int n) {
        int[] factor = new int[2];
        int score = Integer.MAX_VALUE;

        for (int i = 1; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                if (n / i == i) {
                    factor[0] = i;
                    factor[1] = i;
                    break;
                } else {
                    int w = i, h = n / i;
                    int newScore = Math.abs(Math.abs(w) - Math.abs((h)));

                    if (newScore < score) {
                        factor[0] = h < w ? h : w;
                        factor[1] = h < w ? w : h;
                        score = newScore;
                    }
                }
            }
        }

        return factor;
    }
}
