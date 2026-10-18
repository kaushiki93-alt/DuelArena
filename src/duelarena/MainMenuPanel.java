
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

        // Load the menu background image
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

        // Start menu music if music is enabled
        MenuMusic.play();

        // Panel for all menu buttons
        JPanel buttonPanel = new JPanel();

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(
                new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        );

        // ENTER THE BATTLE button
        JButton enterButton =
                createButton("ENTER THE BATTLE");

        // MUSIC ON/OFF button
        musicButton = createButton("");

        updateMusicButtonText();

        // EXIT button
        JButton exitButton = createButton("EXIT");

        // Open Arena Selection
        enterButton.addActionListener(e -> {

            ArenaSelectionPanel selectionPanel =
                    new ArenaSelectionPanel();

            frame.setContentPane(selectionPanel);

            frame.revalidate();
            frame.repaint();
        });

        // Toggle music ON/OFF
        musicButton.addActionListener(e -> {

            MenuMusic.toggleMusic();

            updateMusicButtonText();

            // Resume menu music when switched ON
            if (MenuMusic.isMusicEnabled()) {
                MenuMusic.play();
            }
        });

        // Exit the application
        exitButton.addActionListener(e -> {

            MenuMusic.stop();

            frame.dispose();
        });

        // Add buttons in order
        buttonPanel.add(enterButton);
        buttonPanel.add(Box.createVerticalStrut(15));

        buttonPanel.add(musicButton);
        buttonPanel.add(Box.createVerticalStrut(15));

        buttonPanel.add(exitButton);

        // Position buttons near the bottom
        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 35, 0);

        add(buttonPanel, gbc);
    }

    // Create a styled button
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(new Color(8, 20, 55));

        button.setOpaque(true);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 225, 255), 2
                )
        );

        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setPreferredSize(
                new Dimension(280, 52)
        );

        button.setMaximumSize(
                new Dimension(280, 52)
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // Update the ON/OFF button text
    private void updateMusicButtonText() {

        if (musicButton != null) {

            if (MenuMusic.isMusicEnabled()) {
                musicButton.setText("MUSIC: ON");
            } else {
                musicButton.setText("MUSIC: OFF");
            }
        }
    }

    // Draw the background image
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
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
        }

        g2.dispose();
    }
}
