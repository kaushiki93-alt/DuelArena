package duelarena;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ShieldPowerUp {

    private int x;
    private int y;

    private int size = 30;

    public ShieldPowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Draw the shield power-up
    public void draw(Graphics g) {

        g.setColor(Color.CYAN);

        // Shield shape
        int[] shieldX = {
                x + 15,
                x + 28,
                x + 25,
                x + 15,
                x + 5,
                x + 2
        };

        int[] shieldY = {
                y,
                y + 6,
                y + 20,
                y + 30,
                y + 20,
                y + 6
        };

        g.fillPolygon(shieldX, shieldY, 6);

        // Inner shield
        g.setColor(Color.WHITE);

        int[] innerX = {
                x + 15,
                x + 23,
                x + 21,
                x + 15,
                x + 9,
                x + 7
        };

        int[] innerY = {
                y + 5,
                y + 9,
                y + 18,
                y + 25,
                y + 18,
                y + 9
        };

        g.fillPolygon(innerX, innerY, 6);
    }

    // Used to detect when a player collects the shield
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}