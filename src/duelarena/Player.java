package duelarena;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player {

    private int x;
    private int y;

    private int width = 40;
    private int height = 60;

    private int speed = 5;

    private int health = 100;

    // =========================================================
    // SHIELD STATUS
    // =========================================================

    // Normal shield from Shield Power-up
    private boolean normalShieldActive = false;

    // Adaptive shield from Adaptive Player Balancing
    private boolean adaptiveShieldActive = false;

    private Color color;

    public Player(int x, int y, Color color) {

        this.x = x;
        this.y = y;
        this.color = color;
    }

    // =========================================================
    // MOVEMENT
    // =========================================================

    public void moveLeft() {

        x -= speed;

        if (x < 0) {
            x = 0;
        }
    }

    public void moveRight() {

        x += speed;

        if (x > 760) {
            x = 760;
        }
    }

    public void moveUp() {

        y -= speed;

        // Keep player below health bar
        if (y < 60) {
            y = 60;
        }
    }

    public void moveDown() {

        y += speed;

        // Keep player above arena floor
        if (y > 460) {
            y = 460;
        }
    }

    // =========================================================
    // DRAW PLAYER
    // =========================================================

    public void draw(Graphics g) {

        g.setColor(color);

        g.fillRect(
                x,
                y,
                width,
                height
        );
    }

    // =========================================================
    // COLLISION
    // =========================================================

    public Rectangle getBounds() {

        return new Rectangle(
                x,
                y,
                width,
                height
        );
    }

    // =========================================================
    // HEALTH
    // =========================================================

    public int getHealth() {

        return health;
    }

    // =========================================================
    // TAKE DAMAGE
    // =========================================================

    public void takeDamage(int damage) {

        // Damage is blocked if EITHER shield is active
        if (normalShieldActive || adaptiveShieldActive) {

            return;
        }

        health -= damage;

        if (health < 0) {

            health = 0;
        }
    }

    // =========================================================
    // HEAL
    // =========================================================

    public void heal(int amount) {

        health += amount;

        if (health > 100) {

            health = 100;
        }
    }

    // =========================================================
    // NORMAL SHIELD
    // =========================================================

    public void activateShield() {

        normalShieldActive = true;
    }

    public void deactivateShield() {

        normalShieldActive = false;
    }

    // =========================================================
    // ADAPTIVE SHIELD
    // =========================================================

    public void activateAdaptiveShield() {

        adaptiveShieldActive = true;
    }

    public void deactivateAdaptiveShield() {

        adaptiveShieldActive = false;
    }

    // =========================================================
    // CHECK SHIELD STATUS
    // =========================================================

    public boolean isShieldActive() {

        return normalShieldActive ||
                adaptiveShieldActive;
    }

    public boolean isNormalShieldActive() {

        return normalShieldActive;
    }

    public boolean isAdaptiveShieldActive() {

        return adaptiveShieldActive;
    }

    // =========================================================
    // PLAYER STATUS
    // =========================================================

    public boolean isAlive() {

        return health > 0;
    }

    // =========================================================
    // POSITION
    // =========================================================

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }
}