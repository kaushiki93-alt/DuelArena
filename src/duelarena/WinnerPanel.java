
package duelarena;

import javax.swing.*;
import java.awt.*;

public class WinnerPanel extends JPanel {

    private final String winnerName;
    private final Player winner;
    private final int winningTime;

    // Saved settings for the completed match.
    private final String player1Arena;
    private final String player2Arena;
    private final String player1Name;
    private final String player2Name;
    private final int player1Character;
    private final int player2Character;

    // Logged-in player accounts.
    private final PlayerAccount player1Account;
    private final PlayerAccount player2Account;

    // Original constructor retained for compatibility.
    public WinnerPanel(
            String winnerName,
            Player winner,
            int winningTime,
            String player1Arena,
            String player2Arena,
            String player1Name,
            String player2Name,
            int player1Character,
            int player2Character) {

        this(
                winnerName,
                winner,
                winningTime,
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

    // Updated constructor with player accounts.
    public WinnerPanel(
            String winnerName,
            Player winner,
            int winningTime,
            String player1Arena,
            String player2Arena,
            String player1Name,
            String player2Name,
            int player1Character,
            int player2Character,
            PlayerAccount player1Account,
            PlayerAccount player2Account) {

        this.winnerName = winnerName;
        this.winner = winner;
        this.winningTime = winningTime;

        this.player1Arena = player1Arena;
        this.player2Arena = player2Arena;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Character = player1Character;
        this.player2Character = player2Character;

        this.player1Account = player1Account;
        this.player2Account = player2Account;

        // Victory music.
        SoundManager.playVictoryMusic();

        setLayout(new BorderLayout());

        JPanel content = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int w = getWidth();
                int h = getHeight();

                // Background gradient.
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(18, 25, 55),
                        w, h, new Color(45, 20, 65)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, w, h);

                // Decorative background circles.
                g2.setColor(new Color(255, 215, 0, 30));
                g2.fillOval(w / 2 - 170, 95, 340, 340);

                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(25, 40, 100, 100);
                g2.fillOval(w - 125, 80, 90, 90);

                // Victory heading.
                g2.setColor(new Color(255, 215, 90));
                g2.setFont(new Font("Arial", Font.BOLD, 36));

                drawCentered(g2, "VICTORY!", w / 2, 55);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 19));

                drawCentered(g2, "Congratulations!", w / 2, 88);

                // Winner name.
                g2.setColor(new Color(130, 220, 255));
                g2.setFont(new Font("Arial", Font.BOLD, 29));

                drawCentered(g2, winnerName, w / 2, 135);

                // Winning character card.
                int cardX = w / 2 - 105;
                int cardY = 155;
                int cardW = 210;
                int cardH = 220;

                g2.setColor(new Color(255, 255, 255, 20));

                g2.fillRoundRect(
                        cardX, cardY, cardW, cardH, 25, 25
                );

                g2.setColor(new Color(255, 215, 90));

                g2.drawRoundRect(
                        cardX, cardY, cardW, cardH, 25, 25
                );

                // Actual winning character avatar.
                winner.drawAvatar(
                        g2,
                        w / 2 - 47,
                        cardY + 25,
                        1.8
                );

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));

                drawCentered(
                        g2,
                        Player.CHARACTER_NAMES[winner.getCharacterType()],
                        w / 2,
                        cardY + 195
                );

                // Match statistics card.
                int statsX = w / 2 - 155;
                int statsY = 395;
                int statsW = 310;
                int statsH = 105;

                g2.setColor(new Color(255, 255, 255, 18));

                g2.fillRoundRect(
                        statsX, statsY, statsW, statsH, 18, 18
                );

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 17));

                g2.drawString(
                        "Remaining Health: " + winner.getHealth() + " HP",
                        statsX + 22,
                        statsY + 28
                );

                // Health bar.
                int barX = statsX + 22;
                int barY = statsY + 40;
                int barW = statsW - 44;
                int barH = 17;

                g2.setColor(new Color(90, 40, 50));

                g2.fillRoundRect(
                        barX, barY, barW, barH, 10, 10
                );

                int health = Math.max(
                        0, Math.min(100, winner.getHealth())
                );

                int healthWidth = barW * health / 100;

                if (healthWidth > 0) {

                    g2.setColor(new Color(50, 220, 120));

                    g2.fillRoundRect(
                            barX, barY, healthWidth, barH, 10, 10
                    );
                }

                g2.setColor(Color.WHITE);

                g2.drawRoundRect(
                        barX, barY, barW, barH, 10, 10
                );

                // Winning time.
                g2.setFont(new Font("Arial", Font.BOLD, 17));

                g2.drawString(
                        "Winning Time: " + formatTime(winningTime),
                        statsX + 22,
                        statsY + 83
                );

                g2.dispose();
            }
        };

        content.setOpaque(false);
        content.setLayout(null);

        // PLAY AGAIN button.
        JButton playAgain = new JButton("PLAY AGAIN");

        styleButton(playAgain, new Color(35, 155, 100));

        playAgain.addActionListener(e -> startSameMatch());

        // CHOOSE ARENA / PLAYER button.
        JButton chooseArena =
                new JButton("CHOOSE ARENA / PLAYER");

        styleButton(chooseArena, new Color(70, 100, 200));

        chooseArena.addActionListener(e -> openSelectionScreen());

        // LEADERBOARD button.
        JButton leaderboardButton = new JButton("LEADERBOARD");

        styleButton(leaderboardButton, new Color(180, 130, 35));

        leaderboardButton.addActionListener(e -> {

            JFrame frame =
                    (JFrame) SwingUtilities.getWindowAncestor(this);

            if (frame != null) {

                LeaderboardPanel leaderboardPanel =
                        new LeaderboardPanel(frame);

                frame.setContentPane(leaderboardPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        // Add all three buttons.
        content.add(playAgain);
        content.add(chooseArena);
        content.add(leaderboardButton);

        // Position buttons.
        content.addComponentListener(
                new java.awt.event.ComponentAdapter() {

                    @Override
                    public void componentResized(
                            java.awt.event.ComponentEvent e) {

                        int center = content.getWidth() / 2;

                        playAgain.setBounds(
                                center - 285, 520, 150, 42
                        );

                        chooseArena.setBounds(
                                center - 115, 520, 210, 42
                        );

                        leaderboardButton.setBounds(
                                center + 115, 520, 170, 42
                        );
                    }
                }
        );

        // Initial button positions.
        int center = 400;

        playAgain.setBounds(center - 285, 520, 150, 42);
        chooseArena.setBounds(center - 115, 520, 210, 42);
        leaderboardButton.setBounds(center + 115, 520, 170, 42);

        add(content, BorderLayout.CENTER);
    }

    // =========================
    // PLAY SAME MATCH
    // =========================

    private void startSameMatch() {

        JFrame frame =
                (JFrame) SwingUtilities.getWindowAncestor(this);

        if (frame != null) {

            SoundManager.stopBackgroundMusic();

            GamePanel gamePanel = new GamePanel(
                    player1Arena,
                    player2Arena,
                    player1Name,
                    player2Name,
                    player1Character,
                    player2Character,
                    player1Account,
                    player2Account
            );

            frame.setContentPane(gamePanel);
            frame.revalidate();
            frame.repaint();

            gamePanel.requestFocusInWindow();
        }
    }

    // =========================
    // RETURN TO ARENA SELECTION
    // =========================

    private void openSelectionScreen() {

        JFrame frame =
                (JFrame) SwingUtilities.getWindowAncestor(this);

        if (frame != null) {

            SoundManager.stopBackgroundMusic();

            frame.setContentPane(new ArenaSelectionPanel());
            frame.revalidate();
            frame.repaint();
        }
    }

    // =========================
    // CENTER TEXT
    // =========================

    private void drawCentered(
            Graphics2D g,
            String text,
            int centerX,
            int y) {

        FontMetrics metrics = g.getFontMetrics();

        g.drawString(
                text,
                centerX - metrics.stringWidth(text) / 2,
                y
        );
    }

    // =========================
    // FORMAT WINNING TIME
    // =========================

    private String formatTime(int seconds) {

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        return String.format(
                "%02d:%02d",
                minutes,
                remainingSeconds
        );
    }

    // =========================
    // BUTTON STYLING
    // =========================

    private void styleButton(JButton button, Color color) {

        button.setBackground(color);
        button.setForeground(Color.WHITE);

        button.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
    }
}
