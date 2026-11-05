
package duelarena;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainMenuPanel extends JPanel {

    private final JFrame frame;
    private Image backgroundImage;
    private JButton musicButton;

    public MainMenuPanel(JFrame frame) {

        this.frame = frame;

        setLayout(new GridBagLayout());
        setOpaque(true);

        // Load menu background.
        File imageFile = new File(
                "resources/menu_background/Neon Duel Arena_ Twin Flames.png"
        );

        if (imageFile.exists()) {

            backgroundImage = new ImageIcon(
                    imageFile.getAbsolutePath()
            ).getImage();

        } else {

            System.out.println(
                    "Background image not found: "
                            + imageFile.getAbsolutePath()
            );
        }

        // Start menu music.
        MenuMusic.play();

        // Button panel.
        JPanel buttonPanel = new JPanel();

        buttonPanel.setOpaque(false);

        buttonPanel.setLayout(
                new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        );

        // Enter the battle.
        JButton enterButton = createButton("ENTER THE BATTLE");

        // Music toggle.
        musicButton = createButton("");

        updateMusicButtonText();

        // Exit.
        JButton exitButton = createButton("EXIT");

        // Open arena and player selection.
        enterButton.addActionListener(e -> {

            MenuMusic.stop();

            ArenaSelectionPanel selectionPanel =
                    new ArenaSelectionPanel();

            frame.setContentPane(selectionPanel);
            frame.revalidate();
            frame.repaint();
        });

        // Toggle menu music.
        musicButton.addActionListener(e -> {

            MenuMusic.toggleMusic();

            updateMusicButtonText();

            if (MenuMusic.isMusicEnabled()) {

                MenuMusic.play();

            } else {

                MenuMusic.stop();
            }
        });

        // Exit application.
        exitButton.addActionListener(e -> {

            MenuMusic.stop();

            frame.dispose();

            VoiceManager.shutdown();
        });

        // Add buttons.
        buttonPanel.add(enterButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        buttonPanel.add(musicButton);

        buttonPanel.add(Box.createVerticalStrut(15));

        buttonPanel.add(exitButton);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 35, 0);

        add(buttonPanel, gbc);
    }

    // Create a button with black text.
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        button.setForeground(Color.BLACK);

        button.setBackground(
                new Color(8, 20, 55)
        );

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        // Neon cyan border.
        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(0, 225, 255), 2
                        ),
                        BorderFactory.createEmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setPreferredSize(
                new Dimension(350, 60)
        );

        button.setMinimumSize(
                new Dimension(350, 60)
        );

        button.setMaximumSize(
                new Dimension(350, 60)
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // Update the music button label.
    private void updateMusicButtonText() {

        if (musicButton != null) {

            musicButton.setText(
                    MenuMusic.isMusicEnabled()
                            ? "MUSIC: ON"
                            : "MUSIC: OFF"
            );

            musicButton.setForeground(Color.BLACK);

            musicButton.repaint();
        }
    }

    // Draw the background image.
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        if (backgroundImage != null) {

            g2.drawImage(
                    backgroundImage,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    this
            );

        } else {

            g2.setColor(new Color(5, 8, 25));

            g2.fillRect(
                    0, 0, getWidth(), getHeight()
            );
        }

        g2.dispose();
    }
}
