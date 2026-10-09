package duelarena;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {

    private int x;
    private int y;

    private int direction;
    private int speed = 10;

    private Color color;

    // 1 = Player 1, 2 = Player 2
    private int owner;

    public Bullet(int x, int y, int direction, Color color, int owner) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.color = color;
        this.owner = owner;
    }

    public void move() {
        x += speed * direction;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, 10, 5);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 5);
    }

    public boolean isOffScreen() {
        return x < 0 || x > 800;
    }

    public int getOwner() {
        return owner;
    }
}