package duelarena;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class PowerUp {

    private int x;
    private int y;

    private int size = 30;

    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Draw the heart power-up
    public void draw(Graphics g) {

        g.setColor(Color.RED);

        // Left side of heart
        g.fillOval(x, y, 17, 17);

        // Right side of heart
        g.fillOval(x + 13, y, 17, 17);

        // Bottom part of heart
        int[] heartX = {
                x,
                x + size,
                x + size / 2
        };

        int[] heartY = {
                y + 10,
                y + 10,
                y + size
        };

        g.fillPolygon(heartX, heartY, 3);

        // Small white highlight
        g.setColor(Color.WHITE);
        g.fillOval(x + 8, y + 5, 5, 5);
    }

    // Used to detect when a player touches the power-up
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}