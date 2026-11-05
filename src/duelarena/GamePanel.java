
package duelarena;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {

    private Player player1;
    private Player player2;

    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    // Logged-in player accounts for database integration.
    private PlayerAccount player1Account;
    private PlayerAccount player2Account;

    // Saved settings for Play Again.
    private String selectedPlayer1Arena;
    private String selectedPlayer2Arena;

    private int selectedPlayer1Character;
    private int selectedPlayer2Character;

    private Arena leftArena;
    private Arena rightArena;

    private int player1SoundChoice = 1;
    private int player2SoundChoice = 2;

    private ArrayList<Bullet> bullets;
    private Timer gameTimer;

    private boolean gameOver = false;
    private boolean winnerScreenShown = false;

    // Remains false until the complete AI countdown announcement finishes.
    private boolean gameStarted = false;

    private int matchTime = 45;
    private int timerCounter = 0;

    private int powerUpTimer = 0;
    private int powerUpNumber = 0;

    private final Random random = new Random();

    private PowerUp healthPowerUp;
    private ShieldPowerUp shieldPowerUp;

    private int p1ShieldTimer = 0;
    private int p2ShieldTimer = 0;

    private final int SHIELD_DURATION = 500;

    // Adaptive balancing.
    private boolean adaptiveBalancingActive = false;
    private int adaptivePlayer = 0;
    private int adaptiveCooldown = 0;

    private final int ADAPTIVE_HEALTH_THRESHOLD = 40;
    private final int ADAPTIVE_COOLDOWN = 500;

    private int p1AdaptiveShieldTimer = 0;
    private int p2AdaptiveShieldTimer = 0;

    private final int ADAPTIVE_SHIELD_DURATION = 500;

    // AI voice controls.
    private boolean player1LowHealthAnnounced = false;
    private boolean player2LowHealthAnnounced = false;

    private final int LOW_HEALTH_WARNING = 30;

    private int p1ShootCooldown = 0;
    private int p2ShootCooldown = 0;

    private final int SHOOT_DELAY = 15;

    // Movement controls.
    private boolean p1Up = false;
    private boolean p1Down = false;
    private boolean p1Left = false;
    private boolean p1Right = false;

    private boolean p2Up = false;
    private boolean p2Down = false;
    private boolean p2Left = false;
    private boolean p2Right = false;

    // Sound button positions in the 800 x 600 game area.
    private final int MUSIC_BUTTON_X = 590;
    private final int BULLETS_BUTTON_X = 690;
    private final int SOUND_BUTTON_Y = 48;
    private final int SOUND_BUTTON_WIDTH = 100;
    private final int SOUND_BUTTON_HEIGHT = 25;

    // =========================
    // CONSTRUCTORS
    // =========================

    public GamePanel() {
        this("bg1.png", "Bg2.png", "Player 1", "Player 2", 0, 2);
    }

    public GamePanel(String player1Arena, String player2Arena) {
        this(player1Arena, player2Arena, "Player 1", "Player 2", 0, 2);
    }

    public GamePanel(
            String player1Arena,
            String player2Arena,
            String player1Name,
            String player2Name) {

        this(
                player1Arena,
                player2Arena,
                player1Name,
                player2Name,
                0,
                2
        );
    }

    // Original six-parameter constructor.
    // Kept for compatibility with existing code.
    public GamePanel(
            String player1Arena,
            String player2Arena,
            String player1Name,
            String player2Name,
            int player1Character,
            int player2Character) {

        this(
                player1Arena,
                player2Arena,
                player1Name,
                player2Name,
                player1Character,
                player2Character,
                null,
                null
        );
    }

    // New eight-parameter constructor.
    // Receives the two logged-in accounts from ArenaSelectionPanel.
    public GamePanel(
            String player1Arena,
            String player2Arena,
            String player1Name,
            String player2Name,
            int player1Character,
            int player2Character,
            PlayerAccount player1Account,
            PlayerAccount player2Account) {

        this.player1Account = player1Account;
        this.player2Account = player2Account;

        this.selectedPlayer1Arena = player1Arena;
        this.selectedPlayer2Arena = player2Arena;

        this.selectedPlayer1Character = player1Character;
        this.selectedPlayer2Character = player2Character;

        this.player1Name = cleanName(player1Name, "Player 1");
        this.player2Name = cleanName(player2Name, "Player 2");

        setBackground(Color.DARK_GRAY);
        setFocusable(true);

        leftArena = new Arena("Player 1 Arena", selectedPlayer1Arena);
        rightArena = new Arena("Player 2 Arena", selectedPlayer2Arena);

        // Load sound effects and battle music.
        SoundManager.loadSounds();
        SoundManager.playBackgroundMusic();

        // Create players.
        player1 = new Player(
                100, 250, Color.BLUE, selectedPlayer1Character
        );

        player2 = new Player(
                650, 250, Color.RED, selectedPlayer2Character
        );

        healthPowerUp = null;
        shieldPowerUp = null;

        bullets = new ArrayList<>();

        addKeyListener(this);

        // Mouse controls for music and bullet sounds.
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                double scaleX = getWidth() / 800.0;
                double scaleY = getHeight() / 600.0;

                if (scaleX <= 0 || scaleY <= 0) {
                    return;
                }

                int mouseX = (int) (e.getX() / scaleX);
                int mouseY = (int) (e.getY() / scaleY);

                if (mouseX >= MUSIC_BUTTON_X
                        && mouseX <= MUSIC_BUTTON_X + SOUND_BUTTON_WIDTH
                        && mouseY >= SOUND_BUTTON_Y
                        && mouseY <= SOUND_BUTTON_Y + SOUND_BUTTON_HEIGHT) {

                    boolean enabled =
                            !SoundManager.isBackgroundMusicEnabled();

                    SoundManager.setBackgroundMusicEnabled(enabled);
                    repaint();

                } else if (mouseX >= BULLETS_BUTTON_X
                        && mouseX <= BULLETS_BUTTON_X + SOUND_BUTTON_WIDTH
                        && mouseY >= SOUND_BUTTON_Y
                        && mouseY <= SOUND_BUTTON_Y + SOUND_BUTTON_HEIGHT) {

                    boolean enabled =
                            !SoundManager.areBulletSoundsEnabled();

                    SoundManager.setBulletSoundsEnabled(enabled);
                    repaint();
                }

                requestFocusInWindow();
            }
        });

        // Prepare the timer, but DO NOT start it during the AI announcement.
        gameTimer = new Timer(20, e -> updateGame());

        // Clear any previous gameplay announcement, then speak the full intro.
        VoiceManager.cancelSpeech();

        VoiceManager.speakAndWait(
                "We have entered the battlefield. " +
                        this.player1Name + " versus " + this.player2Name + ". " +
                        "Three, two, one. Game starts now!"
        ).whenComplete((ignored, error) -> SwingUtilities.invokeLater(() -> {
            if (!gameOver) {
                gameStarted = true;
                gameTimer.start();
                requestFocusInWindow();
                repaint();
            }
        }));

        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    private String cleanName(String name, String defaultName) {

        if (name == null || name.trim().isEmpty()) {
            return defaultName;
        }

        return name.trim();
    }

    // =========================
    // UPDATE GAME
    // =========================

    private void updateGame() {

        if (gameOver || !gameStarted) {
            return;
        }

        timerCounter++;

        if (timerCounter >= 50) {

            matchTime--;
            timerCounter = 0;

            if (matchTime <= 0) {
                matchTime = 0;
                finishGame();
                return;
            }
        }

        powerUpTimer++;

        if (powerUpTimer >= 500 && matchTime > 0) {
            powerUpTimer = 0;
            spawnRandomPowerUp();
        }

        if (p1ShootCooldown > 0) {
            p1ShootCooldown--;
        }

        if (p2ShootCooldown > 0) {
            p2ShootCooldown--;
        }

        // Normal shield timers.
        if (p1ShieldTimer > 0) {

            p1ShieldTimer--;

            if (p1ShieldTimer == 0) {
                player1.deactivateShield();
                System.out.println(player1Name + " Normal Shield Expired!");
            }
        }

        if (p2ShieldTimer > 0) {

            p2ShieldTimer--;

            if (p2ShieldTimer == 0) {
                player2.deactivateShield();
                System.out.println(player2Name + " Normal Shield Expired!");
            }
        }

        // Player 1 movement.
        if (p1Up) player1.moveUp();
        if (p1Down) player1.moveDown();
        if (p1Left) player1.moveLeft();
        if (p1Right) player1.moveRight();

        // Player 2 movement.
        if (p2Up) player2.moveUp();
        if (p2Down) player2.moveDown();
        if (p2Left) player2.moveLeft();
        if (p2Right) player2.moveRight();

        // Move bullets.
        for (Bullet bullet : bullets) {
            bullet.move();
        }

        checkBulletCollisions();

        bullets.removeIf(Bullet::isOffScreen);

        // Check defeat immediately after damage.
        if (!player1.isAlive() || !player2.isAlive()) {
            finishGame();
            return;
        }

        checkPowerUpCollision();

        // AI voice warning when health becomes low.
        checkLowHealthVoice();

        // Check adaptive balancing only while both players are alive.
        checkAdaptiveBalancing();

        if (!player1.isAlive() || !player2.isAlive()) {
            finishGame();
            return;
        }

        repaint();
    }

    // =========================
    // LOW-HEALTH VOICE WARNINGS
    // =========================

    private void checkLowHealthVoice() {

        if (player1.getHealth() <= LOW_HEALTH_WARNING
                && player1.isAlive()
                && !player1LowHealthAnnounced) {

            player1LowHealthAnnounced = true;

            VoiceManager.speak(
                    player1Name +
                            ", protect yourself! Your health is critically low!"
            );
        }

        if (player2.getHealth() <= LOW_HEALTH_WARNING
                && player2.isAlive()
                && !player2LowHealthAnnounced) {

            player2LowHealthAnnounced = true;

            VoiceManager.speak(
                    player2Name +
                            ", protect yourself! Your health is critically low!"
            );
        }
    }

    // =========================
    // FINISH MATCH
    // =========================

    private void finishGame() {

        if (gameOver) {
            return;
        }

        gameOver = true;
        gameStarted = false;

        if (gameTimer != null) {
            gameTimer.stop();
        }

        VoiceManager.cancelSpeech();
        SoundManager.stopBackgroundMusic();

        SwingUtilities.invokeLater(this::showWinnerScreen);
    }

    // =========================
    // WINNER SCREEN + DATABASE
    // =========================

    private void showWinnerScreen() {

        if (winnerScreenShown) {
            return;
        }

        winnerScreenShown = true;

        Player winner = null;
        PlayerAccount winnerAccount = null;
        String winnerName = "";

        if (!player1.isAlive() && !player2.isAlive()) {

            showDrawScreen();
            return;

        } else if (!player1.isAlive()) {

            winner = player2;
            winnerName = player2Name;
            winnerAccount = player2Account;

        } else if (!player2.isAlive()) {

            winner = player1;
            winnerName = player1Name;
            winnerAccount = player1Account;

        } else if (player1.getHealth() > player2.getHealth()) {

            winner = player1;
            winnerName = player1Name;
            winnerAccount = player1Account;

        } else if (player2.getHealth() > player1.getHealth()) {

            winner = player2;
            winnerName = player2Name;
            winnerAccount = player2Account;

        } else {

            showDrawScreen();
            return;
        }

        // Save match details only when both players are logged in.
        if (player1Account != null
                && player2Account != null
                && winnerAccount != null) {

            MatchHistoryDAO historyDAO = new MatchHistoryDAO();

            boolean saved = historyDAO.recordMatch(
                    player1Account,
                    player2Account,
                    winnerAccount,
                    String.valueOf(selectedPlayer1Character),
                    String.valueOf(selectedPlayer2Character),
                    100
            );

            if (saved) {
                System.out.println(
                        "Match history saved successfully!"
                );
                System.out.println(
                        winnerName + " received 100 points."
                );
            } else {
                System.out.println(
                        "Could not save match history."
                );
            }

        } else {

            System.out.println(
                    "Match history not saved: player accounts are missing."
            );
        }

        JFrame frame = (JFrame)
                SwingUtilities.getWindowAncestor(this);

        if (frame == null) {
            return;
        }

        int winningTime = 45 - matchTime;

        WinnerPanel winnerPanel = new WinnerPanel(
                winnerName,
                winner,
                winningTime,
                selectedPlayer1Arena,
                selectedPlayer2Arena,
                player1Name,
                player2Name,
                selectedPlayer1Character,
                selectedPlayer2Character,
                player1Account,
                player2Account
        );

        frame.setContentPane(winnerPanel);
        frame.revalidate();
        frame.repaint();

        VoiceManager.speakAndWait(
                "Congratulations, " + winnerName + "! You won!"
        );
    }

    // =========================
    // DRAW SCREEN
    // =========================

    private void showDrawScreen() {

        JFrame frame = (JFrame)
                SwingUtilities.getWindowAncestor(this);

        if (frame == null) {
            return;
        }

        VoiceManager.cancelSpeech();
        SoundManager.stopBackgroundMusic();

        JOptionPane.showMessageDialog(
                frame,
                "The match ended in a draw!",
                "Match Draw",
                JOptionPane.INFORMATION_MESSAGE
        );

        frame.setContentPane(new ArenaSelectionPanel());
        frame.revalidate();
        frame.repaint();
    }

    // =========================
    // RANDOM POWER-UP
    // =========================

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

    // =========================
    // POWER-UP COLLISIONS
    // =========================

    private void checkPowerUpCollision() {

        if (healthPowerUp != null) {

            if (healthPowerUp.getBounds().intersects(
                    player1.getBounds())) {

                player1.heal(20);
                healthPowerUp = null;

                System.out.println(
                        player1Name + " collected Health Power-up!"
                );

                System.out.println(
                        player1Name + " Health: " + player1.getHealth()
                );

            } else if (healthPowerUp.getBounds().intersects(
                    player2.getBounds())) {

                player2.heal(20);
                healthPowerUp = null;

                System.out.println(
                        player2Name + " collected Health Power-up!"
                );

                System.out.println(
                        player2Name + " Health: " + player2.getHealth()
                );
            }
        }

        if (shieldPowerUp != null) {

            if (shieldPowerUp.getBounds().intersects(
                    player1.getBounds())) {

                player1.activateShield();
                p1ShieldTimer = SHIELD_DURATION;
                shieldPowerUp = null;

                System.out.println(
                        player1Name + " collected Normal Shield!"
                );

                System.out.println(
                        "Normal Shield active for 10 seconds."
                );

            } else if (shieldPowerUp.getBounds().intersects(
                    player2.getBounds())) {

                player2.activateShield();
                p2ShieldTimer = SHIELD_DURATION;
                shieldPowerUp = null;

                System.out.println(
                        player2Name + " collected Normal Shield!"
                );

                System.out.println(
                        "Normal Shield active for 10 seconds."
                );
            }
        }
    }

    // =========================
    // ADAPTIVE PLAYER BALANCING
    // =========================

    private void checkAdaptiveBalancing() {

        if (adaptiveCooldown > 0) {
            adaptiveCooldown--;
        }

        if (p1AdaptiveShieldTimer > 0) {

            p1AdaptiveShieldTimer--;

            if (p1AdaptiveShieldTimer == 0) {
                player1.deactivateAdaptiveShield();

                System.out.println(
                        player1Name + " Adaptive Shield Expired!"
                );
            }
        }

        if (p2AdaptiveShieldTimer > 0) {

            p2AdaptiveShieldTimer--;

            if (p2AdaptiveShieldTimer == 0) {
                player2.deactivateAdaptiveShield();

                System.out.println(
                        player2Name + " Adaptive Shield Expired!"
                );
            }
        }

        if (adaptiveBalancingActive) return;
        if (adaptiveCooldown > 0) return;

        int player1Health = player1.getHealth();
        int player2Health = player2.getHealth();

        if (player1Health <= ADAPTIVE_HEALTH_THRESHOLD
                && player1Health < player2Health) {

            adaptiveBalancingActive = true;
            adaptivePlayer = 1;

            player1.heal(15);
            player1.activateAdaptiveShield();

            p1AdaptiveShieldTimer = ADAPTIVE_SHIELD_DURATION;

            System.out.println(
                    "ADAPTIVE BALANCING ACTIVATED for " +
                            player1Name + "!"
            );

            System.out.println(
                    player1Name +
                            " received +15 HP and 10-second Adaptive Shield."
            );

            VoiceManager.cancelSpeech();
            VoiceManager.speak(
                    "Adaptive balancing activated! " +
                            player1Name +
                            ", you have received extra protection!"
            );

            adaptiveCooldown = ADAPTIVE_COOLDOWN;
            return;
        }

        if (player2Health <= ADAPTIVE_HEALTH_THRESHOLD
                && player2Health < player1Health) {

            adaptiveBalancingActive = true;
            adaptivePlayer = 2;

            player2.heal(15);
            player2.activateAdaptiveShield();

            p2AdaptiveShieldTimer = ADAPTIVE_SHIELD_DURATION;

            System.out.println(
                    "ADAPTIVE BALANCING ACTIVATED for " +
                            player2Name + "!"
            );

            System.out.println(
                    player2Name +
                            " received +15 HP and 10-second Adaptive Shield."
            );

            VoiceManager.cancelSpeech();
            VoiceManager.speak(
                    "Adaptive balancing activated! " +
                            player2Name +
                            ", you have received extra protection!"
            );

            adaptiveCooldown = ADAPTIVE_COOLDOWN;
        }
    }

    // =========================
    // BULLET COLLISIONS
    // =========================

    private void checkBulletCollisions() {

        for (int i = bullets.size() - 1; i >= 0; i--) {

            Bullet bullet = bullets.get(i);

            if (bullet.getOwner() == 1
                    && bullet.getBounds().intersects(
                    player2.getBounds())) {

                player2.takeDamage(10);
                bullets.remove(i);

                System.out.println(
                        player2Name + " Health: " + player2.getHealth()
                );

            } else if (bullet.getOwner() == 2
                    && bullet.getBounds().intersects(
                    player1.getBounds())) {

                player1.takeDamage(10);
                bullets.remove(i);

                System.out.println(
                        player1Name + " Health: " + player1.getHealth()
                );
            }
        }
    }

    // =========================
    // DRAW GAME
    // =========================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        double scaleX = getWidth() / 800.0;
        double scaleY = getHeight() / 600.0;

        g2.scale(scaleX, scaleY);

        leftArena.draw(g2, 0, 0, 400, 600);
        rightArena.draw(g2, 400, 0, 400, 600);

        // Floor.
        g2.setColor(new Color(45, 45, 45));
        g2.fillRect(0, 520, 800, 80);

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 518, 800, 2);

        g2.setColor(new Color(100, 100, 100));
        g2.fillRect(399, 60, 2, 458);

        g2.setColor(new Color(80, 80, 80));
        g2.fillRect(100, 540, 120, 5);
        g2.fillRect(580, 540, 120, 5);

        // Player names.
        g2.setFont(new Font("Arial", Font.BOLD, 16));

        g2.setColor(Color.CYAN);
        g2.drawString(player1Name, 100, 245);

        g2.setColor(Color.PINK);
        g2.drawString(player2Name, 650, 245);

        // Players.
        player1.draw(g2);
        player2.draw(g2);

        // Shields.
        if (player1.isShieldActive()) {

            g2.setColor(Color.CYAN);

            g2.drawOval(
                    player1.getX() - 10,
                    player1.getY() - 10,
                    60,
                    80
            );
        }

        if (player2.isShieldActive()) {

            g2.setColor(Color.CYAN);

            g2.drawOval(
                    player2.getX() - 10,
                    player2.getY() - 10,
                    60,
                    80
            );
        }

        // Power-ups.
        if (healthPowerUp != null) {
            healthPowerUp.draw(g2);
        }

        if (shieldPowerUp != null) {
            shieldPowerUp.draw(g2);
        }

        // Bullets.
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }

        // Health bars.
        drawHealthBar(g2, 20, 20, player1.getHealth());
        drawHealthBar(g2, 600, 20, player2.getHealth());

        // Match timer.
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("TIME: " + matchTime, 350, 35);

        if (!gameStarted && !gameOver) {
            g2.setColor(new Color(0, 0, 0, 175));
            g2.fillRoundRect(190, 255, 420, 80, 18, 18);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String readyText = "GET READY - MATCH STARTING";
            java.awt.FontMetrics readyMetrics = g2.getFontMetrics();
            int readyX = (800 - readyMetrics.stringWidth(readyText)) / 2;
            g2.drawString(readyText, readyX, 302);
        }

        // Music toggle.
        drawSoundButton(
                g2,
                MUSIC_BUTTON_X,
                SOUND_BUTTON_Y,
                SOUND_BUTTON_WIDTH,
                SOUND_BUTTON_HEIGHT,
                "Music: " +
                        (SoundManager.isBackgroundMusicEnabled()
                                ? "ON" : "OFF"),
                SoundManager.isBackgroundMusicEnabled()
        );

        // Bullet sound toggle.
        drawSoundButton(
                g2,
                BULLETS_BUTTON_X,
                SOUND_BUTTON_Y,
                SOUND_BUTTON_WIDTH,
                SOUND_BUTTON_HEIGHT,
                "Bullets: " +
                        (SoundManager.areBulletSoundsEnabled()
                                ? "ON" : "OFF"),
                SoundManager.areBulletSoundsEnabled()
        );

        // Adaptive balancing message.
        if (adaptiveBalancingActive) {

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            if (adaptivePlayer == 1) {

                g2.drawString(
                        "ADAPTIVE BALANCING: " + player1Name,
                        245, 80
                );

            } else if (adaptivePlayer == 2) {

                g2.drawString(
                        "ADAPTIVE BALANCING: " + player2Name,
                        245, 80
                );
            }
        }

        g2.dispose();
    }

    // =========================
    // DRAW SOUND TOGGLE BUTTON
    // =========================

    private void drawSoundButton(
            Graphics2D g2,
            int x,
            int y,
            int width,
            int height,
            String text,
            boolean enabled) {

        g2.setColor(enabled
                ? new Color(35, 115, 65)
                : new Color(130, 45, 45));

        g2.fillRoundRect(x, y, width, height, 8, 8);

        g2.setColor(Color.WHITE);
        g2.drawRoundRect(x, y, width, height, 8, 8);

        g2.setFont(new Font("Arial", Font.BOLD, 12));

        java.awt.FontMetrics metrics = g2.getFontMetrics();

        int textX = x + (width - metrics.stringWidth(text)) / 2;

        int textY = y + (height - metrics.getHeight()) / 2
                + metrics.getAscent();

        g2.drawString(text, textX, textY);
    }

    // =========================
    // HEALTH BAR
    // =========================

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

    // =========================
    // RETURN TO ARENA SELECTION
    // =========================

    private void restartGame() {

        if (gameTimer != null) {
            gameTimer.stop();
        }

        VoiceManager.cancelSpeech();
        SoundManager.stopBackgroundMusic();

        JFrame frame = (JFrame)
                SwingUtilities.getWindowAncestor(this);

        if (frame != null) {

            frame.setContentPane(new ArenaSelectionPanel());
            frame.revalidate();
            frame.repaint();
        }
    }

    // =========================
    // KEY PRESSED
    // =========================

    @Override
    public void keyPressed(KeyEvent e) {

        // Ignore all gameplay controls until the AI countdown finishes.
        if (!gameStarted && !gameOver) {
            return;
        }

        if (gameOver) {

            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            }

            return;
        }

        int key = e.getKeyCode();

        // Player 1 movement.
        if (key == KeyEvent.VK_W) p1Up = true;
        if (key == KeyEvent.VK_S) p1Down = true;
        if (key == KeyEvent.VK_A) p1Left = true;
        if (key == KeyEvent.VK_D) p1Right = true;

        // Player 2 movement.
        if (key == KeyEvent.VK_UP) p2Up = true;
        if (key == KeyEvent.VK_DOWN) p2Down = true;
        if (key == KeyEvent.VK_LEFT) p2Left = true;
        if (key == KeyEvent.VK_RIGHT) p2Right = true;

        // Player 1 shooting.
        if (key == KeyEvent.VK_F && p1ShootCooldown == 0) {

            Bullet bullet = new Bullet(
                    player1.getX() + 40,
                    player1.getY() + 25,
                    1,
                    Color.BLUE,
                    1
            );

            bullets.add(bullet);
            SoundManager.playSound(player1SoundChoice - 1);

            p1ShootCooldown = SHOOT_DELAY;
        }

        // Player 2 shooting.
        if (key == KeyEvent.VK_L && p2ShootCooldown == 0) {

            Bullet bullet = new Bullet(
                    player2.getX(),
                    player2.getY() + 25,
                    -1,
                    Color.RED,
                    2
            );

            bullets.add(bullet);
            SoundManager.playSound(player2SoundChoice - 1);

            p2ShootCooldown = SHOOT_DELAY;
        }
    }

    // =========================
    // KEY RELEASED
    // =========================

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) p1Up = false;
        if (key == KeyEvent.VK_S) p1Down = false;
        if (key == KeyEvent.VK_A) p1Left = false;
        if (key == KeyEvent.VK_D) p1Right = false;

        if (key == KeyEvent.VK_UP) p2Up = false;
        if (key == KeyEvent.VK_DOWN) p2Down = false;
        if (key == KeyEvent.VK_LEFT) p2Left = false;
        if (key == KeyEvent.VK_RIGHT) p2Right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not required for the current controls.
    }
}
