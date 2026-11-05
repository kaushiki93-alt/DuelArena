
package duelarena;

import java.awt.*;
import java.awt.geom.*;

public class Player {

    private int x;
    private int y;

    private final int width = 40;
    private final int height = 60;

    private int speed = 5;
    private int health = 100;

    private boolean normalShieldActive = false;
    private boolean adaptiveShieldActive = false;

    private Color color;
    private int characterType;

    public static final String[] CHARACTER_NAMES = {
            "Classic Fighter",
            "Girl Fighter",
            "Ninja",
            "Panda"
    };

    public Player(int x, int y, Color color) {
        this(x, y, color, 0);
    }

    public Player(int x, int y, Color color, int characterType) {

        this.x = x;
        this.y = y;
        this.color = color;

        if (characterType < 0
                || characterType >= CHARACTER_NAMES.length) {
            characterType = 0;
        }

        this.characterType = characterType;
    }

    // =========================
    // MOVEMENT
    // =========================

    public void moveLeft() {
        x -= speed;
        if (x < 0) x = 0;
    }

    public void moveRight() {
        x += speed;
        if (x > 760) x = 760;
    }

    public void moveUp() {
        y -= speed;
        if (y < 60) y = 60;
    }

    public void moveDown() {
        y += speed;
        if (y > 460) y = 460;
    }

    // =========================
    // DRAW CHARACTER AT NORMAL GAME POSITION
    // =========================

    public void draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.translate(x, y);
        g2.scale(0.8, 0.85);

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawSelectedCharacter(g2);

        g2.dispose();
    }

    // =========================
    // DRAW LARGER AVATAR
    // =========================

    public void drawAvatar(
            Graphics2D graphics,
            int drawX,
            int drawY,
            double scale) {

        Graphics2D g2 =
                (Graphics2D) graphics.create();

        g2.translate(drawX, drawY);
        g2.scale(scale, scale);

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawSelectedCharacter(g2);

        g2.dispose();
    }

    // =========================
    // DRAW SELECTED CHARACTER
    // =========================

    private void drawSelectedCharacter(Graphics2D g2) {

        switch (characterType) {

            case 0:
                drawClassicFighter(g2);
                break;

            case 1:
                drawGirlFighter(g2);
                break;

            case 2:
                drawNinja(g2);
                break;

            case 3:
                drawPanda(g2);
                break;

            default:
                drawClassicFighter(g2);
        }
    }

    // =========================
    // CHARACTER 1: CLASSIC FIGHTER
    // =========================

    private void drawClassicFighter(Graphics2D g) {

        Color skin = new Color(255, 207, 160);
        Color hair = new Color(35, 27, 25);
        Color dark = new Color(25, 30, 45);

        // Legs
        g.setColor(dark);
        g.fillRoundRect(11, 48, 12, 18, 5, 5);
        g.fillRoundRect(28, 48, 12, 18, 5, 5);

        // Boots
        g.setColor(color.darker());
        g.fillRoundRect(7, 61, 18, 8, 4, 4);
        g.fillRoundRect(27, 61, 18, 8, 4, 4);

        // Body
        g.setColor(color);
        g.fillRoundRect(8, 28, 36, 26, 8, 8);

        // Shirt highlight
        g.setColor(color.brighter());
        g.fillRoundRect(15, 31, 22, 18, 5, 5);

        // Shirt stripe
        g.setColor(Color.WHITE);
        g.fillRect(25, 32, 3, 17);

        // Arms
        g.setColor(skin);
        g.fillRoundRect(1, 31, 10, 19, 5, 5);
        g.fillRoundRect(41, 31, 10, 19, 5, 5);

        // Gloves
        g.setColor(color.darker());
        g.fillRoundRect(0, 44, 12, 11, 4, 4);
        g.fillRoundRect(40, 44, 12, 11, 4, 4);

        // Neck
        g.setColor(skin);
        g.fillRect(21, 23, 10, 8);

        // Face
        g.fillOval(12, 4, 28, 27);

        // Hair
        g.setColor(hair);
        g.fillArc(10, 0, 32, 22, 0, 180);

        g.fillPolygon(
                new int[]{11, 17, 24, 31, 40},
                new int[]{14, 5, 12, 4, 14},
                5
        );

        drawEyes(g, 18, 15, 30, 15);

        // Smile
        g.setColor(new Color(125, 45, 35));
        g.drawArc(21, 18, 11, 6, 200, 140);

        // Belt
        g.setColor(Color.WHITE);
        g.fillRect(12, 48, 28, 3);
    }

    // =========================
    // CHARACTER 2: GIRL FIGHTER
    // =========================

    private void drawGirlFighter(Graphics2D g) {

        Color skin = new Color(255, 210, 175);
        Color hair = new Color(75, 30, 80);

        // Long hair behind the body
        g.setColor(hair);
        g.fillRoundRect(7, 3, 38, 39, 15, 15);
        g.fillRoundRect(5, 18, 10, 30, 7, 7);
        g.fillRoundRect(37, 18, 10, 30, 7, 7);

        // Legs
        g.setColor(skin);
        g.fillRoundRect(13, 49, 10, 17, 5, 5);
        g.fillRoundRect(29, 49, 10, 17, 5, 5);

        // Boots
        g.setColor(color.darker());
        g.fillRoundRect(8, 61, 17, 8, 4, 4);
        g.fillRoundRect(27, 61, 17, 8, 4, 4);

        // Body
        g.setColor(color);
        g.fillRoundRect(8, 29, 36, 25, 9, 9);

        // Jacket design
        g.setColor(color.brighter());

        g.fillPolygon(
                new int[]{14, 25, 36},
                new int[]{30, 43, 30},
                3
        );

        // Arms
        g.setColor(skin);
        g.fillRoundRect(1, 32, 10, 18, 5, 5);
        g.fillRoundRect(41, 32, 10, 18, 5, 5);

        // Gloves
        g.setColor(color.darker());
        g.fillOval(0, 44, 12, 11);
        g.fillOval(40, 44, 12, 11);

        // Neck
        g.setColor(skin);
        g.fillRect(21, 23, 10, 8);

        // Face
        g.fillOval(13, 4, 27, 27);

        // Hair fringe
        g.setColor(hair);
        g.fillArc(10, 0, 33, 22, 0, 180);

        g.fillPolygon(
                new int[]{12, 19, 26, 34, 41},
                new int[]{13, 6, 15, 7, 15},
                5
        );

        drawEyes(g, 19, 16, 31, 16);

        // Hair accessory
        g.setColor(Color.YELLOW);
        g.fillOval(35, 19, 6, 6);
    }

    // =========================
    // CHARACTER 3: NINJA
    // =========================

    private void drawNinja(Graphics2D g) {

        Color dark = new Color(22, 23, 35);
        Color mask = new Color(55, 58, 70);

        // Legs
        g.setColor(dark);
        g.fillRoundRect(10, 48, 13, 19, 4, 4);
        g.fillRoundRect(28, 48, 13, 19, 4, 4);

        // Boots
        g.setColor(color.darker());
        g.fillRoundRect(7, 62, 18, 7, 3, 3);
        g.fillRoundRect(27, 62, 18, 7, 3, 3);

        // Body
        g.setColor(dark);
        g.fillRoundRect(8, 27, 36, 29, 7, 7);

        // Ninja tunic
        g.setColor(color);

        g.fillPolygon(
                new int[]{11, 25, 42, 38, 12},
                new int[]{29, 35, 29, 52, 52},
                5
        );

        // Crossed straps
        g.setColor(new Color(180, 180, 190));
        g.setStroke(new BasicStroke(3));

        g.drawLine(15, 31, 35, 49);
        g.drawLine(35, 31, 15, 49);

        // Arms
        g.setColor(dark);
        g.fillRoundRect(1, 32, 10, 19, 4, 4);
        g.fillRoundRect(41, 32, 10, 19, 4, 4);

        // Gloves
        g.setColor(color);
        g.fillRoundRect(0, 46, 12, 9, 3, 3);
        g.fillRoundRect(40, 46, 12, 9, 3, 3);

        // Hood
        g.setColor(dark);
        g.fillOval(9, 0, 34, 34);

        // Face mask
        g.setColor(mask);
        g.fillRoundRect(13, 13, 27, 16, 5, 5);

        // Eyes
        g.setColor(Color.WHITE);
        g.fillOval(17, 12, 10, 6);
        g.fillOval(29, 12, 10, 6);

        g.setColor(Color.BLACK);
        g.fillOval(21, 13, 4, 4);
        g.fillOval(33, 13, 4, 4);

        // Headband
        g.setColor(color.brighter());
        g.fillRect(11, 8, 30, 4);
    }

    // =========================
    // CHARACTER 4: PANDA
    // =========================

    private void drawPanda(Graphics2D g) {

        Color black = new Color(30, 35, 45);
        Color white = new Color(245, 245, 245);

        // Ears
        g.setColor(black);
        g.fillOval(8, 0, 15, 15);
        g.fillOval(31, 0, 15, 15);

        // Legs
        g.fillRoundRect(11, 49, 13, 17, 5, 5);
        g.fillRoundRect(28, 49, 13, 17, 5, 5);

        // Feet
        g.fillRoundRect(7, 61, 19, 8, 5, 5);
        g.fillRoundRect(26, 61, 19, 8, 5, 5);

        // Arms
        g.fillRoundRect(0, 30, 14, 23, 8, 8);
        g.fillRoundRect(38, 30, 14, 23, 8, 8);

        // Body
        g.fillRoundRect(7, 27, 38, 31, 13, 13);

        // White belly
        g.setColor(white);
        g.fillOval(15, 34, 22, 21);

        // Head
        g.fillOval(9, 3, 34, 32);

        // Eye patches
        g.setColor(black);
        g.fillOval(14, 12, 11, 12);
        g.fillOval(28, 12, 11, 12);

        // Eyes
        g.setColor(Color.WHITE);
        g.fillOval(17, 15, 5, 5);
        g.fillOval(31, 15, 5, 5);

        g.setColor(Color.BLACK);
        g.fillOval(19, 16, 2, 3);
        g.fillOval(33, 16, 2, 3);

        // Nose
        g.fillOval(24, 22, 5, 4);

        // Mouth
        g.setStroke(new BasicStroke(1.5f));
        g.drawArc(21, 24, 10, 6, 200, 140);

        // Team-colored belt
        g.setColor(color);
        g.fillRoundRect(12, 49, 28, 4, 2, 2);
    }

    // =========================
    // COMMON EYE DRAWING
    // =========================

    private void drawEyes(
            Graphics2D g,
            int x1,
            int y1,
            int x2,
            int y2) {

        g.setColor(Color.WHITE);
        g.fillOval(x1, y1, 7, 7);
        g.fillOval(x2, y2, 7, 7);

        g.setColor(Color.BLACK);
        g.fillOval(x1 + 3, y1 + 2, 3, 4);
        g.fillOval(x2 + 3, y2 + 2, 3, 4);
    }

    // =========================
    // COLLISION BOUNDS
    // =========================

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // =========================
    // HEALTH
    // =========================

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {

        if (normalShieldActive || adaptiveShieldActive) {
            return;
        }

        health -= damage;

        if (health < 0) {
            health = 0;
        }
    }

    public void heal(int amount) {

        health += amount;

        if (health > 100) {
            health = 100;
        }
    }

    // =========================
    // NORMAL SHIELD
    // =========================

    public void activateShield() {
        normalShieldActive = true;
    }

    public void deactivateShield() {
        normalShieldActive = false;
    }

    // =========================
    // ADAPTIVE SHIELD
    // =========================

    public void activateAdaptiveShield() {
        adaptiveShieldActive = true;
    }

    public void deactivateAdaptiveShield() {
        adaptiveShieldActive = false;
    }

    public boolean isShieldActive() {
        return normalShieldActive || adaptiveShieldActive;
    }

    public boolean isNormalShieldActive() {
        return normalShieldActive;
    }

    public boolean isAdaptiveShieldActive() {
        return adaptiveShieldActive;
    }

    // =========================
    // PLAYER STATUS
    // =========================

    public boolean isAlive() {
        return health > 0;
    }

    // =========================
    // POSITION
    // =========================

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // =========================
    // CHARACTER
    // =========================

    public int getCharacterType() {
        return characterType;
    }

    // =========================
    // LEADERBOARD AVATAR
    // =========================

    public static Player createLeaderboardAvatar(int characterType) {

        Color avatarColor;

        switch (characterType) {

            case 0:
                avatarColor = new Color(40, 130, 220);
                break;

            case 1:
                avatarColor = new Color(210, 80, 170);
                break;

            case 2:
                avatarColor = new Color(80, 85, 110);
                break;

            case 3:
                avatarColor = new Color(230, 170, 50);
                break;

            default:
                avatarColor = Color.BLUE;
        }

        return new Player(
                0,
                0,
                avatarColor,
                characterType
        );
    }
}
