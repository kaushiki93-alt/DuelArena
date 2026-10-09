package duelarena;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    private Player player1;
    private Player player2;

    private ArrayList<Bullet> bullets;

    private Timer gameTimer;

    private boolean gameOver = false;

    // =========================================================
    // MATCH TIMER
    // =========================================================

    private int matchTime = 45;
    private int timerCounter = 0;

    // =========================================================
    // POWER-UP TIMER
    // =========================================================

    // Power-up appears every 10 seconds
    private int powerUpTimer = 0;

    // Used to alternate between Heart and Shield
    private int powerUpNumber = 0;

    private Random random = new Random();

    // =========================================================
    // POWER-UPS
    // =========================================================

    private PowerUp healthPowerUp;
    private ShieldPowerUp shieldPowerUp;

    // =========================================================
    // NORMAL SHIELD
    // =========================================================

    private int p1ShieldTimer = 0;
    private int p2ShieldTimer = 0;

    // 500 × 20 milliseconds = approximately 10 seconds
    private final int SHIELD_DURATION = 500;

    // =========================================================
    // ADAPTIVE PLAYER BALANCING
    // =========================================================

    private boolean adaptiveBalancingActive = false;

    // 1 = Player 1
    // 2 = Player 2
    private int adaptivePlayer = 0;

    // Cooldown before adaptive balancing can activate again
    private int adaptiveCooldown = 0;

    // Adaptive balancing activates when losing player
    // reaches 40 HP or below
    private final int ADAPTIVE_HEALTH_THRESHOLD = 40;

    // 500 × 20 milliseconds = approximately 10 seconds
    private final int ADAPTIVE_COOLDOWN = 500;

    // =========================================================
    // ADAPTIVE SHIELD
    // =========================================================

    private int p1AdaptiveShieldTimer = 0;
    private int p2AdaptiveShieldTimer = 0;

    // 500 × 20 milliseconds = approximately 10 seconds
    private final int ADAPTIVE_SHIELD_DURATION = 500;

    // =========================================================
    // SHOOTING
    // =========================================================

    private int p1ShootCooldown = 0;
    private int p2ShootCooldown = 0;

    private final int SHOOT_DELAY = 15;

    // =========================================================
    // PLAYER 1 MOVEMENT
    // =========================================================

    private boolean p1Up = false;
    private boolean p1Down = false;
    private boolean p1Left = false;
    private boolean p1Right = false;

    // =========================================================
    // PLAYER 2 MOVEMENT
    // =========================================================

    private boolean p2Up = false;
    private boolean p2Down = false;
    private boolean p2Left = false;
    private boolean p2Right = false;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public GamePanel() {

        setBackground(Color.DARK_GRAY);

        player1 = new Player(
                100,
                250,
                Color.BLUE
        );

        player2 = new Player(
                650,
                250,
                Color.RED
        );

        // Power-ups start empty.
        // They will appear randomly during the match.
        healthPowerUp = null;
        shieldPowerUp = null;

        bullets = new ArrayList<>();

        setFocusable(true);
        addKeyListener(this);

        gameTimer = new Timer(
                20,
                e -> updateGame()
        );

        gameTimer.start();
    }

    // =========================================================
    // UPDATE GAME
    // =========================================================

    private void updateGame() {

        if (gameOver) {
            return;
        }

        // =====================================================
        // MATCH TIMER
        // =====================================================

        timerCounter++;

        // 50 updates × 20 ms = approximately 1 second
        if (timerCounter >= 50) {

            matchTime--;

            timerCounter = 0;

            if (matchTime <= 0) {

                matchTime = 0;

                gameOver = true;

                gameTimer.stop();
            }
        }

        // =====================================================
        // POWER-UP SPAWN TIMER
        // =====================================================

        powerUpTimer++;

        // 500 updates × 20 ms = approximately 10 seconds
        if (powerUpTimer >= 500 &&
                matchTime > 0) {

            powerUpTimer = 0;

            spawnRandomPowerUp();
        }

        // =====================================================
        // SHOOTING COOLDOWN
        // =====================================================

        if (p1ShootCooldown > 0) {
            p1ShootCooldown--;
        }

        if (p2ShootCooldown > 0) {
            p2ShootCooldown--;
        }

        // =====================================================
        // NORMAL SHIELD TIMER
        // =====================================================

        if (p1ShieldTimer > 0) {

            p1ShieldTimer--;

            if (p1ShieldTimer == 0) {

                player1.deactivateShield();

                System.out.println(
                        "Player 1 Normal Shield Expired!"
                );
            }
        }

        if (p2ShieldTimer > 0) {

            p2ShieldTimer--;

            if (p2ShieldTimer == 0) {

                player2.deactivateShield();

                System.out.println(
                        "Player 2 Normal Shield Expired!"
                );
            }
        }

        // =====================================================
        // PLAYER 1 MOVEMENT
        // =====================================================

        if (p1Up) {
            player1.moveUp();
        }

        if (p1Down) {
            player1.moveDown();
        }

        if (p1Left) {
            player1.moveLeft();
        }

        if (p1Right) {
            player1.moveRight();
        }

        // =====================================================
        // PLAYER 2 MOVEMENT
        // =====================================================

        if (p2Up) {
            player2.moveUp();
        }

        if (p2Down) {
            player2.moveDown();
        }

        if (p2Left) {
            player2.moveLeft();
        }

        if (p2Right) {
            player2.moveRight();
        }

        // =====================================================
        // MOVE BULLETS
        // =====================================================

        for (Bullet bullet : bullets) {

            bullet.move();
        }

        // =====================================================
        // BULLET COLLISIONS
        // =====================================================

        checkBulletCollisions();

        // Remove bullets outside screen
        bullets.removeIf(Bullet::isOffScreen);

        // =====================================================
        // POWER-UP COLLISION
        // =====================================================

        checkPowerUpCollision();

        // =====================================================
        // ADAPTIVE BALANCING
        // =====================================================

        checkAdaptiveBalancing();

        // =====================================================
        // GAME OVER
        // =====================================================

        if (!player1.isAlive() ||
                !player2.isAlive()) {

            gameOver = true;

            gameTimer.stop();
        }

        repaint();
    }

    // =========================================================
    // RANDOM POWER-UP SPAWN
    // =========================================================

    private void spawnRandomPowerUp() {

        // Random location inside playable arena
        //
        // X: 50 to 749
        // Y: 100 to 449
        //
        // This keeps power-ups away from the very edge,
        // health bars and floor.

        int x = 50 + random.nextInt(700);
        int y = 100 + random.nextInt(350);

        powerUpNumber++;

        // Alternate:
        // 1 = Heart
        // 2 = Shield
        // 3 = Heart
        // 4 = Shield

        if (powerUpNumber % 2 == 1) {

            healthPowerUp = new PowerUp(
                    x,
                    y
            );

            shieldPowerUp = null;

            System.out.println(
                    "Health Power-up appeared at: "
                            + x + ", " + y
            );

        } else {

            shieldPowerUp = new ShieldPowerUp(
                    x,
                    y
            );

            healthPowerUp = null;

            System.out.println(
                    "Shield Power-up appeared at: "
                            + x + ", " + y
            );
        }
    }

    // =========================================================
    // POWER-UP COLLISION
    // =========================================================

    private void checkPowerUpCollision() {

        // =====================================================
        // HEALTH POWER-UP
        // =====================================================

        if (healthPowerUp != null) {

            // Player 1 collects health
            if (healthPowerUp.getBounds()
                    .intersects(player1.getBounds())) {

                player1.heal(20);

                healthPowerUp = null;

                System.out.println(
                        "Player 1 collected Health Power-up!"
                );

                System.out.println(
                        "Player 1 Health: "
                                + player1.getHealth()
                );
            }

            // Player 2 collects health
            else if (healthPowerUp.getBounds()
                    .intersects(player2.getBounds())) {

                player2.heal(20);

                healthPowerUp = null;

                System.out.println(
                        "Player 2 collected Health Power-up!"
                );

                System.out.println(
                        "Player 2 Health: "
                                + player2.getHealth()
                );
            }
        }

        // =====================================================
        // NORMAL SHIELD POWER-UP
        // =====================================================

        if (shieldPowerUp != null) {

            // Player 1 collects shield
            if (shieldPowerUp.getBounds()
                    .intersects(player1.getBounds())) {

                player1.activateShield();

                p1ShieldTimer =
                        SHIELD_DURATION;

                shieldPowerUp = null;

                System.out.println(
                        "Player 1 collected Normal Shield!"
                );

                System.out.println(
                        "Normal Shield active for 10 seconds."
                );
            }

            // Player 2 collects shield
            else if (shieldPowerUp.getBounds()
                    .intersects(player2.getBounds())) {

                player2.activateShield();

                p2ShieldTimer =
                        SHIELD_DURATION;

                shieldPowerUp = null;

                System.out.println(
                        "Player 2 collected Normal Shield!"
                );

                System.out.println(
                        "Normal Shield active for 10 seconds."
                );
            }
        }
    }

    // =========================================================
    // ADAPTIVE PLAYER BALANCING
    // =========================================================

    private void checkAdaptiveBalancing() {

        // =====================================================
        // REDUCE ADAPTIVE COOLDOWN
        // =====================================================

        if (adaptiveCooldown > 0) {

            adaptiveCooldown--;
        }

        // =====================================================
        // PLAYER 1 ADAPTIVE SHIELD TIMER
        // =====================================================

        if (p1AdaptiveShieldTimer > 0) {

            p1AdaptiveShieldTimer--;

            if (p1AdaptiveShieldTimer == 0) {

                player1.deactivateAdaptiveShield();

                System.out.println(
                        "Player 1 Adaptive Shield Expired!"
                );
            }
        }

        // =====================================================
        // PLAYER 2 ADAPTIVE SHIELD TIMER
        // =====================================================

        if (p2AdaptiveShieldTimer > 0) {

            p2AdaptiveShieldTimer--;

            if (p2AdaptiveShieldTimer == 0) {

                player2.deactivateAdaptiveShield();

                System.out.println(
                        "Player 2 Adaptive Shield Expired!"
                );
            }
        }

        // =====================================================
        // ALREADY ACTIVE
        // =====================================================

        if (adaptiveBalancingActive) {

            return;
        }

        // =====================================================
        // COOLDOWN ACTIVE
        // =====================================================

        if (adaptiveCooldown > 0) {

            return;
        }

        int player1Health =
                player1.getHealth();

        int player2Health =
                player2.getHealth();

        // =====================================================
        // PLAYER 1 IS LOSING AND HAS 40 HP OR LESS
        // =====================================================

        if (player1Health <=
                ADAPTIVE_HEALTH_THRESHOLD &&
                player1Health < player2Health) {

            adaptiveBalancingActive = true;

            adaptivePlayer = 1;

            // Give 15 HP
            player1.heal(15);

            // Give Adaptive Shield
            player1.activateAdaptiveShield();

            p1AdaptiveShieldTimer =
                    ADAPTIVE_SHIELD_DURATION;

            System.out.println(
                    "ADAPTIVE BALANCING ACTIVATED "
                            + "for PLAYER 1!"
            );

            System.out.println(
                    "Player 1 received +15 HP "
                            + "and 10-second Adaptive Shield."
            );

            // Start cooldown
            adaptiveCooldown =
                    ADAPTIVE_COOLDOWN;

            return;
        }

        // =====================================================
        // PLAYER 2 IS LOSING AND HAS 40 HP OR LESS
        // =====================================================

        if (player2Health <=
                ADAPTIVE_HEALTH_THRESHOLD &&
                player2Health < player1Health) {

            adaptiveBalancingActive = true;

            adaptivePlayer = 2;

            // Give 15 HP
            player2.heal(15);

            // Give Adaptive Shield
            player2.activateAdaptiveShield();

            p2AdaptiveShieldTimer =
                    ADAPTIVE_SHIELD_DURATION;

            System.out.println(
                    "ADAPTIVE BALANCING ACTIVATED "
                            + "for PLAYER 2!"
            );

            System.out.println(
                    "Player 2 received +15 HP "
                            + "and 10-second Adaptive Shield."
            );

            // Start cooldown
            adaptiveCooldown =
                    ADAPTIVE_COOLDOWN;
        }
    }

    // =========================================================
    // BULLET COLLISION
    // =========================================================

    private void checkBulletCollisions() {

        for (int i = bullets.size() - 1;
             i >= 0;
             i--) {

            Bullet bullet = bullets.get(i);

            // =================================================
            // PLAYER 1 BULLET HITS PLAYER 2
            // =================================================

            if (bullet.getOwner() == 1 &&
                    bullet.getBounds()
                            .intersects(
                                    player2.getBounds()
                            )) {

                player2.takeDamage(10);

                bullets.remove(i);

                System.out.println(
                        "Player 2 Health: "
                                + player2.getHealth()
                );
            }

            // =================================================
            // PLAYER 2 BULLET HITS PLAYER 1
            // =================================================

            else if (bullet.getOwner() == 2 &&
                    bullet.getBounds()
                            .intersects(
                                    player1.getBounds()
                            )) {

                player1.takeDamage(10);

                bullets.remove(i);

                System.out.println(
                        "Player 1 Health: "
                                + player1.getHealth()
                );
            }
        }
    }

    // =========================================================
    // DRAW GAME
    // =========================================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // =====================================================
        // SCALE GAME TO WINDOW SIZE
        // =====================================================

        Graphics2D g2 =
                (Graphics2D) g.create();

        double scaleX =
                getWidth() / 800.0;

        double scaleY =
                getHeight() / 600.0;

        g2.scale(
                scaleX,
                scaleY
        );

        g = g2;

        // =====================================================
        // ARENA FLOOR
        // =====================================================

        g.setColor(
                new Color(45, 45, 45)
        );

        g.fillRect(
                0,
                520,
                800,
                80
        );

        // Floor line
        g.setColor(Color.WHITE);

        g.fillRect(
                0,
                518,
                800,
                2
        );

        // Center line
        g.setColor(
                new Color(100, 100, 100)
        );

        g.fillRect(
                399,
                60,
                2,
                458
        );

        // Floor markings
        g.setColor(
                new Color(80, 80, 80)
        );

        g.fillRect(
                100,
                540,
                120,
                5
        );

        g.fillRect(
                580,
                540,
                120,
                5
        );

        // =====================================================
        // PLAYER NAMES
        // =====================================================

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        g.drawString(
                "PLAYER 1",
                100,
                245
        );

        g.drawString(
                "PLAYER 2",
                650,
                245
        );

        // =====================================================
        // DRAW PLAYERS
        // =====================================================

        player1.draw(g);

        player2.draw(g);

        // =====================================================
        // DRAW PLAYER 1 SHIELD
        // =====================================================

        if (player1.isShieldActive()) {

            g.setColor(Color.CYAN);

            g.drawOval(
                    player1.getX() - 10,
                    player1.getY() - 10,
                    60,
                    80
            );
        }

        // =====================================================
        // DRAW PLAYER 2 SHIELD
        // =====================================================

        if (player2.isShieldActive()) {

            g.setColor(Color.CYAN);

            g.drawOval(
                    player2.getX() - 10,
                    player2.getY() - 10,
                    60,
                    80
            );
        }

        // =====================================================
        // HEALTH POWER-UP
        // =====================================================

        if (healthPowerUp != null) {

            healthPowerUp.draw(g);
        }

        // =====================================================
        // SHIELD POWER-UP
        // =====================================================

        if (shieldPowerUp != null) {

            shieldPowerUp.draw(g);
        }

        // =====================================================
        // BULLETS
        // =====================================================

        for (Bullet bullet : bullets) {

            bullet.draw(g);
        }

        // =====================================================
        // HEALTH BARS
        // =====================================================

        drawHealthBar(
                g,
                20,
                20,
                player1.getHealth()
        );

        drawHealthBar(
                g,
                600,
                20,
                player2.getHealth()
        );

        // =====================================================
        // MATCH TIMER
        // =====================================================

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        String timeText =
                "TIME: " + matchTime;

        g.drawString(
                timeText,
                350,
                35
        );

        // =====================================================
        // ADAPTIVE BALANCING MESSAGE
        // =====================================================

        if (adaptiveBalancingActive) {

            g.setColor(Color.YELLOW);

            g.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            if (adaptivePlayer == 1) {

                g.drawString(
                        "ADAPTIVE BALANCING: PLAYER 1",
                        245,
                        80
                );

            } else if (adaptivePlayer == 2) {

                g.drawString(
                        "ADAPTIVE BALANCING: PLAYER 2",
                        245,
                        80
                );
            }
        }

        // =====================================================
        // GAME OVER
        // =====================================================

        if (gameOver) {

            g.setColor(Color.WHITE);

            g.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            40
                    )
            );

            String message;

            if (!player1.isAlive()) {

                message = "PLAYER 2 WINS!";

            } else if (!player2.isAlive()) {

                message = "PLAYER 1 WINS!";

            } else if (matchTime == 0) {

                if (player1.getHealth() >
                        player2.getHealth()) {

                    message = "PLAYER 1 WINS!";

                } else if (player2.getHealth() >
                        player1.getHealth()) {

                    message = "PLAYER 2 WINS!";

                } else {

                    message = "DRAW!";
                }

            } else {

                message = "GAME OVER!";
            }

            g.drawString(
                    message,
                    250,
                    300
            );

            g.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            20
                    )
            );

            g.drawString(
                    "Press R to Restart",
                    300,
                    340
            );
        }

        // =====================================================
        // DISPOSE GRAPHICS
        // =====================================================

        g2.dispose();
    }

    // =========================================================
    // HEALTH BAR
    // =========================================================

    private void drawHealthBar(
            Graphics g,
            int x,
            int y,
            int health) {

        // Empty health bar
        g.setColor(Color.RED);

        g.fillRect(
                x,
                y,
                180,
                20
        );

        // Current health
        g.setColor(Color.GREEN);

        g.fillRect(
                x,
                y,
                (health * 180) / 100,
                20
        );

        // Border
        g.setColor(Color.WHITE);

        g.drawRect(
                x,
                y,
                180,
                20
        );

        // Health text
        g.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        g.drawString(
                health + " HP",
                x + 70,
                y + 15
        );
    }

    // =========================================================
    // RESTART GAME
    // =========================================================

    private void restartGame() {

        // Create new players
        player1 = new Player(
                100,
                250,
                Color.BLUE
        );

        player2 = new Player(
                650,
                250,
                Color.RED
        );

        // Clear bullets
        bullets.clear();

        // Reset power-ups
        healthPowerUp = null;
        shieldPowerUp = null;

        powerUpTimer = 0;
        powerUpNumber = 0;

        // Reset normal shield timers
        p1ShieldTimer = 0;
        p2ShieldTimer = 0;

        // Reset adaptive balancing
        adaptiveBalancingActive = false;
        adaptivePlayer = 0;
        adaptiveCooldown = 0;

        // Reset adaptive shield timers
        p1AdaptiveShieldTimer = 0;
        p2AdaptiveShieldTimer = 0;

        // Reset shooting cooldown
        p1ShootCooldown = 0;
        p2ShootCooldown = 0;

        // Reset match timer to 45 seconds
        matchTime = 45;
        timerCounter = 0;

        // Reset movement keys
        p1Up = false;
        p1Down = false;
        p1Left = false;
        p1Right = false;

        p2Up = false;
        p2Down = false;
        p2Left = false;
        p2Right = false;

        // Reset game over
        gameOver = false;

        // Start timer
        gameTimer.start();

        repaint();
    }

    // =========================================================
    // KEY PRESSED
    // =========================================================

    @Override
    public void keyPressed(KeyEvent e) {

        // Restart after game over
        if (gameOver) {

            if (e.getKeyCode() ==
                    KeyEvent.VK_R) {

                restartGame();
            }

            return;
        }

        int key = e.getKeyCode();

        // =====================================================
        // PLAYER 1 MOVEMENT
        // =====================================================

        if (key == KeyEvent.VK_W) {

            p1Up = true;
        }

        if (key == KeyEvent.VK_S) {

            p1Down = true;
        }

        if (key == KeyEvent.VK_A) {

            p1Left = true;
        }

        if (key == KeyEvent.VK_D) {

            p1Right = true;
        }

        // =====================================================
        // PLAYER 2 MOVEMENT
        // =====================================================

        if (key == KeyEvent.VK_UP) {

            p2Up = true;
        }

        if (key == KeyEvent.VK_DOWN) {

            p2Down = true;
        }

        if (key == KeyEvent.VK_LEFT) {

            p2Left = true;
        }

        if (key == KeyEvent.VK_RIGHT) {

            p2Right = true;
        }

        // =====================================================
        // PLAYER 1 SHOOTING
        // =====================================================

        if (key == KeyEvent.VK_F &&
                p1ShootCooldown == 0) {

            Bullet bullet = new Bullet(
                    player1.getX() + 40,
                    player1.getY() + 25,
                    1,
                    Color.BLUE,
                    1
            );

            bullets.add(bullet);

            p1ShootCooldown =
                    SHOOT_DELAY;
        }

        // =====================================================
        // PLAYER 2 SHOOTING
        // =====================================================

        if (key == KeyEvent.VK_L &&
                p2ShootCooldown == 0) {

            Bullet bullet = new Bullet(
                    player2.getX(),
                    player2.getY() + 25,
                    -1,
                    Color.RED,
                    2
            );

            bullets.add(bullet);

            p2ShootCooldown =
                    SHOOT_DELAY;
        }
    }

    // =========================================================
    // KEY RELEASED
    // =========================================================

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        // =====================================================
        // PLAYER 1
        // =====================================================

        if (key == KeyEvent.VK_W) {

            p1Up = false;
        }

        if (key == KeyEvent.VK_S) {

            p1Down = false;
        }

        if (key == KeyEvent.VK_A) {

            p1Left = false;
        }

        if (key == KeyEvent.VK_D) {

            p1Right = false;
        }

        // =====================================================
        // PLAYER 2
        // =====================================================

        if (key == KeyEvent.VK_UP) {

            p2Up = false;
        }

        if (key == KeyEvent.VK_DOWN) {

            p2Down = false;
        }

        if (key == KeyEvent.VK_LEFT) {

            p2Left = false;
        }

        if (key == KeyEvent.VK_RIGHT) {

            p2Right = false;
        }
    }

    // =========================================================
    // KEY TYPED
    // =========================================================

    @Override
    public void keyTyped(KeyEvent e) {
    }
}