
package duelarena;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
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

    // Selected arenas
    private Arena leftArena;
    private Arena rightArena;

    // Default bullet sounds
    private int player1SoundChoice = 1;
    private int player2SoundChoice = 2;

    private ArrayList<Bullet> bullets;
    private Timer gameTimer;

    private boolean gameOver = false;

    // MATCH TIMER
    private int matchTime = 45;
    private int timerCounter = 0;

    // POWER-UP TIMER
    private int powerUpTimer = 0;
    private int powerUpNumber = 0;

    private Random random = new Random();

    // POWER-UPS
    private PowerUp healthPowerUp;
    private ShieldPowerUp shieldPowerUp;

    // NORMAL SHIELD
    private int p1ShieldTimer = 0;
    private int p2ShieldTimer = 0;

    private final int SHIELD_DURATION = 500;

    // ADAPTIVE PLAYER BALANCING
    private boolean adaptiveBalancingActive = false;
    private int adaptivePlayer = 0;
    private int adaptiveCooldown = 0;

    private final int ADAPTIVE_HEALTH_THRESHOLD = 40;
    private final int ADAPTIVE_COOLDOWN = 500;

    // ADAPTIVE SHIELD
    private int p1AdaptiveShieldTimer = 0;
    private int p2AdaptiveShieldTimer = 0;

    private final int ADAPTIVE_SHIELD_DURATION = 500;

    // SHOOTING
    private int p1ShootCooldown = 0;
    private int p2ShootCooldown = 0;

    private final int SHOOT_DELAY = 15;

    // PLAYER 1 MOVEMENT
    private boolean p1Up = false;
    private boolean p1Down = false;
    private boolean p1Left = false;
    private boolean p1Right = false;

    // PLAYER 2 MOVEMENT
    private boolean p2Up = false;
    private boolean p2Down = false;
    private boolean p2Left = false;
    private boolean p2Right = false;

    // CONSTRUCTOR
    public GamePanel() {
        this("bg1.png", "bg2.png");
    }

    public GamePanel(String player1Arena, String player2Arena) {

        setBackground(Color.DARK_GRAY);

        // Load selected backgrounds
        leftArena = new Arena("Player 1 Arena", player1Arena);
        rightArena = new Arena("Player 2 Arena", player2Arena);

        // Load sound effects
        SoundManager.loadSounds();

        player1 = new Player(100, 250, Color.BLUE);
        player2 = new Player(650, 250, Color.RED);

        healthPowerUp = null;
        shieldPowerUp = null;

        bullets = new ArrayList<>();

        setFocusable(true);
        addKeyListener(this);

        gameTimer = new Timer(20, e -> updateGame());
        gameTimer.start();
    }

    // UPDATE GAME
    private void updateGame() {

        if (gameOver) {
            return;
        }

        // MATCH TIMER
        timerCounter++;

        if (timerCounter >= 50) {
            matchTime--;
            timerCounter = 0;

            if (matchTime <= 0) {
                matchTime = 0;
                gameOver = true;
                gameTimer.stop();
            }
        }

        // POWER-UP SPAWN TIMER
        powerUpTimer++;

        if (powerUpTimer >= 500 && matchTime > 0) {
            powerUpTimer = 0;
            spawnRandomPowerUp();
        }

        // SHOOTING COOLDOWN
        if (p1ShootCooldown > 0) {
            p1ShootCooldown--;
        }

        if (p2ShootCooldown > 0) {
            p2ShootCooldown--;
        }

        // NORMAL SHIELD TIMER
        if (p1ShieldTimer > 0) {
            p1ShieldTimer--;

            if (p1ShieldTimer == 0) {
                player1.deactivateShield();
                System.out.println("Player 1 Normal Shield Expired!");
            }
        }

        if (p2ShieldTimer > 0) {
            p2ShieldTimer--;

            if (p2ShieldTimer == 0) {
                player2.deactivateShield();
                System.out.println("Player 2 Normal Shield Expired!");
            }
        }

        // PLAYER 1 MOVEMENT
        if (p1Up) player1.moveUp();
        if (p1Down) player1.moveDown();
        if (p1Left) player1.moveLeft();
        if (p1Right) player1.moveRight();

        // PLAYER 2 MOVEMENT
        if (p2Up) player2.moveUp();
        if (p2Down) player2.moveDown();
        if (p2Left) player2.moveLeft();
        if (p2Right) player2.moveRight();

        // MOVE BULLETS
        for (Bullet bullet : bullets) {
            bullet.move();
        }

        // BULLET COLLISIONS
        checkBulletCollisions();

        bullets.removeIf(Bullet::isOffScreen);

        // POWER-UP COLLISION
        checkPowerUpCollision();

        // ADAPTIVE BALANCING
        checkAdaptiveBalancing();

        // GAME OVER
        if (!player1.isAlive() || !player2.isAlive()) {
            gameOver = true;
            gameTimer.stop();
        }

        repaint();
    }

    // RANDOM POWER-UP SPAWN
    private void spawnRandomPowerUp() {

        int x = 50 + random.nextInt(700);
        int y = 100 + random.nextInt(350);

        powerUpNumber++;

        if (powerUpNumber % 2 == 1) {

            healthPowerUp = new PowerUp(x, y);
            shieldPowerUp = null;

            System.out.println(
                    "Health Power-up appeared at: " + x + ", " + y
            );

        } else {

            shieldPowerUp = new ShieldPowerUp(x, y);
            healthPowerUp = null;

            System.out.println(
                    "Shield Power-up appeared at: " + x + ", " + y
            );
        }
    }

    // POWER-UP COLLISION
    private void checkPowerUpCollision() {

        // HEALTH POWER-UP
        if (healthPowerUp != null) {

            if (healthPowerUp.getBounds()
                    .intersects(player1.getBounds())) {

                player1.heal(20);
                healthPowerUp = null;

                System.out.println("Player 1 collected Health Power-up!");
                System.out.println("Player 1 Health: " + player1.getHealth());

            } else if (healthPowerUp.getBounds()
                    .intersects(player2.getBounds())) {

                player2.heal(20);
                healthPowerUp = null;

                System.out.println("Player 2 collected Health Power-up!");
                System.out.println("Player 2 Health: " + player2.getHealth());
            }
        }

        // NORMAL SHIELD POWER-UP
        if (shieldPowerUp != null) {

            if (shieldPowerUp.getBounds()
                    .intersects(player1.getBounds())) {

                player1.activateShield();
                p1ShieldTimer = SHIELD_DURATION;
                shieldPowerUp = null;

                System.out.println("Player 1 collected Normal Shield!");
                System.out.println("Normal Shield active for 10 seconds.");

            } else if (shieldPowerUp.getBounds()
                    .intersects(player2.getBounds())) {

                player2.activateShield();
                p2ShieldTimer = SHIELD_DURATION;
                shieldPowerUp = null;

                System.out.println("Player 2 collected Normal Shield!");
                System.out.println("Normal Shield active for 10 seconds.");
            }
        }
    }

    // ADAPTIVE PLAYER BALANCING
    private void checkAdaptiveBalancing() {

        if (adaptiveCooldown > 0) {
            adaptiveCooldown--;
        }

        // PLAYER 1 ADAPTIVE SHIELD TIMER
        if (p1AdaptiveShieldTimer > 0) {
            p1AdaptiveShieldTimer--;

            if (p1AdaptiveShieldTimer == 0) {
                player1.deactivateAdaptiveShield();
                System.out.println("Player 1 Adaptive Shield Expired!");
            }
        }

        // PLAYER 2 ADAPTIVE SHIELD TIMER
        if (p2AdaptiveShieldTimer > 0) {
            p2AdaptiveShieldTimer--;

            if (p2AdaptiveShieldTimer == 0) {
                player2.deactivateAdaptiveShield();
                System.out.println("Player 2 Adaptive Shield Expired!");
            }
        }

        if (adaptiveBalancingActive) {
            return;
        }

        if (adaptiveCooldown > 0) {
            return;
        }

        int player1Health = player1.getHealth();
        int player2Health = player2.getHealth();

        // PLAYER 1 IS LOSING
        if (player1Health <= ADAPTIVE_HEALTH_THRESHOLD
                && player1Health < player2Health) {

            adaptiveBalancingActive = true;
            adaptivePlayer = 1;

            player1.heal(15);
            player1.activateAdaptiveShield();

            p1AdaptiveShieldTimer = ADAPTIVE_SHIELD_DURATION;

            System.out.println(
                    "ADAPTIVE BALANCING ACTIVATED for PLAYER 1!"
            );

            System.out.println(
                    "Player 1 received +15 HP and 10-second Adaptive Shield."
            );

            adaptiveCooldown = ADAPTIVE_COOLDOWN;
            return;
        }

        // PLAYER 2 IS LOSING
        if (player2Health <= ADAPTIVE_HEALTH_THRESHOLD
                && player2Health < player1Health) {

            adaptiveBalancingActive = true;
            adaptivePlayer = 2;

            player2.heal(15);
            player2.activateAdaptiveShield();

            p2AdaptiveShieldTimer = ADAPTIVE_SHIELD_DURATION;

            System.out.println(
                    "ADAPTIVE BALANCING ACTIVATED for PLAYER 2!"
            );

            System.out.println(
                    "Player 2 received +15 HP and 10-second Adaptive Shield."
            );

            adaptiveCooldown = ADAPTIVE_COOLDOWN;
        }
    }

    // BULLET COLLISION
    private void checkBulletCollisions() {

        for (int i = bullets.size() - 1; i >= 0; i--) {

            Bullet bullet = bullets.get(i);

            // PLAYER 1 BULLET HITS PLAYER 2
            if (bullet.getOwner() == 1
                    && bullet.getBounds()
                    .intersects(player2.getBounds())) {

                player2.takeDamage(10);
                bullets.remove(i);

                System.out.println("Player 2 Health: " + player2.getHealth());
            }

            // PLAYER 2 BULLET HITS PLAYER 1
            else if (bullet.getOwner() == 2
                    && bullet.getBounds()
                    .intersects(player1.getBounds())) {

                player1.takeDamage(10);
                bullets.remove(i);

                System.out.println("Player 1 Health: " + player1.getHealth());
            }
        }
    }

    // DRAW GAME
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        double scaleX = getWidth() / 800.0;
        double scaleY = getHeight() / 600.0;

        g2.scale(scaleX, scaleY);

        // DRAW SELECTED ARENAS FIRST
        leftArena.draw(g2, 0, 0, 400, 600);
        rightArena.draw(g2, 400, 0, 400, 600);

        // ARENA FLOOR
        g2.setColor(new Color(45, 45, 45));
        g2.fillRect(0, 520, 800, 80);

        // FLOOR LINE
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 518, 800, 2);

        // CENTER LINE
        g2.setColor(new Color(100, 100, 100));
        g2.fillRect(399, 60, 2, 458);

        // FLOOR MARKINGS
        g2.setColor(new Color(80, 80, 80));
        g2.fillRect(100, 540, 120, 5);
        g2.fillRect(580, 540, 120, 5);

        // PLAYER NAMES
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));

        g2.drawString("PLAYER 1", 100, 245);
        g2.drawString("PLAYER 2", 650, 245);

        // DRAW PLAYERS
        player1.draw(g2);
        player2.draw(g2);

        // PLAYER 1 SHIELD
        if (player1.isShieldActive()) {
            g2.setColor(Color.CYAN);
            g2.drawOval(
                    player1.getX() - 10,
                    player1.getY() - 10,
                    60,
                    80
            );
        }

        // PLAYER 2 SHIELD
        if (player2.isShieldActive()) {
            g2.setColor(Color.CYAN);
            g2.drawOval(
                    player2.getX() - 10,
                    player2.getY() - 10,
                    60,
                    80
            );
        }

        // HEALTH POWER-UP
        if (healthPowerUp != null) {
            healthPowerUp.draw(g2);
        }

        // SHIELD POWER-UP
        if (shieldPowerUp != null) {
            shieldPowerUp.draw(g2);
        }

        // BULLETS
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }

        // HEALTH BARS
        drawHealthBar(g2, 20, 20, player1.getHealth());
        drawHealthBar(g2, 600, 20, player2.getHealth());

        // MATCH TIMER
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));

        g2.drawString("TIME: " + matchTime, 350, 35);

        // ADAPTIVE BALANCING MESSAGE
        if (adaptiveBalancingActive) {

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            if (adaptivePlayer == 1) {
                g2.drawString(
                        "ADAPTIVE BALANCING: PLAYER 1",
                        245,
                        80
                );

            } else if (adaptivePlayer == 2) {
                g2.drawString(
                        "ADAPTIVE BALANCING: PLAYER 2",
                        245,
                        80
                );
            }
        }

        // GAME OVER
        if (gameOver) {

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 40));

            String message;

            if (!player1.isAlive()) {
                message = "PLAYER 2 WINS!";

            } else if (!player2.isAlive()) {
                message = "PLAYER 1 WINS!";

            } else if (matchTime == 0) {

                if (player1.getHealth() > player2.getHealth()) {
                    message = "PLAYER 1 WINS!";

                } else if (player2.getHealth() > player1.getHealth()) {
                    message = "PLAYER 2 WINS!";

                } else {
                    message = "DRAW!";
                }

            } else {
                message = "GAME OVER!";
            }

            g2.drawString(message, 250, 300);

            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("Press R to Choose Arenas Again", 225, 340);
        }

        g2.dispose();
    }

    // HEALTH BAR
    private void drawHealthBar(
            Graphics g,
            int x,
            int y,
            int health) {

        g.setColor(Color.RED);
        g.fillRect(x, y, 180, 20);

        g.setColor(Color.GREEN);
        g.fillRect(x, y, (health * 180) / 100, 20);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, 180, 20);

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString(health + " HP", x + 70, y + 15);
    }

    // RETURN TO ARENA SELECTION SCREEN
    private void restartGame() {

        // Stop the current game's timer
        gameTimer.stop();

        // Get the current game window
        JFrame frame = (JFrame)
                SwingUtilities.getWindowAncestor(this);

        // Open arena selection screen again
        if (frame != null) {

            ArenaSelectionPanel selectionPanel =
                    new ArenaSelectionPanel();

            frame.setContentPane(selectionPanel);
            frame.revalidate();
            frame.repaint();
        }
    }

    // KEY PRESSED
    @Override
    public void keyPressed(KeyEvent e) {

        if (gameOver) {

            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }

            return;
        }

        int key = e.getKeyCode();

        // PLAYER 1 MOVEMENT
        if (key == KeyEvent.VK_W) p1Up = true;
        if (key == KeyEvent.VK_S) p1Down = true;
        if (key == KeyEvent.VK_A) p1Left = true;
        if (key == KeyEvent.VK_D) p1Right = true;

        // PLAYER 2 MOVEMENT
        if (key == KeyEvent.VK_UP) p2Up = true;
        if (key == KeyEvent.VK_DOWN) p2Down = true;
        if (key == KeyEvent.VK_LEFT) p2Left = true;
        if (key == KeyEvent.VK_RIGHT) p2Right = true;

        // PLAYER 1 SHOOTING
        if (key == KeyEvent.VK_F && p1ShootCooldown == 0) {

            Bullet bullet = new Bullet(
                    player1.getX() + 40,
                    player1.getY() + 25,
                    1,
                    Color.BLUE,
                    1
            );

            bullets.add(bullet);
            SoundManager.playBulletSound(player1SoundChoice);
            p1ShootCooldown = SHOOT_DELAY;
        }

        // PLAYER 2 SHOOTING
        if (key == KeyEvent.VK_L && p2ShootCooldown == 0) {

            Bullet bullet = new Bullet(
                    player2.getX(),
                    player2.getY() + 25,
                    -1,
                    Color.RED,
                    2
            );

            bullets.add(bullet);
            SoundManager.playBulletSound(player2SoundChoice);
            p2ShootCooldown = SHOOT_DELAY;
        }
    }

    // KEY RELEASED
    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        // PLAYER 1
        if (key == KeyEvent.VK_W) p1Up = false;
        if (key == KeyEvent.VK_S) p1Down = false;
        if (key == KeyEvent.VK_A) p1Left = false;
        if (key == KeyEvent.VK_D) p1Right = false;

        // PLAYER 2
        if (key == KeyEvent.VK_UP) p2Up = false;
        if (key == KeyEvent.VK_DOWN) p2Down = false;
        if (key == KeyEvent.VK_LEFT) p2Left = false;
        if (key == KeyEvent.VK_RIGHT) p2Right = false;
    }

    // KEY TYPED
    @Override
    public void keyTyped(KeyEvent e) {
    }
}
