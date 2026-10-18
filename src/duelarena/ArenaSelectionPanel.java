
package duelarena;

import javax.swing.*;
import java.awt.*;

public class ArenaSelectionPanel extends JPanel {

    private JComboBox<String> player1Arena;
    private JComboBox<String> player2Arena;

    private final String[] arenas = {
            "bg1.png", "bg2.png", "bg3.png",
            "bg4.png", "bg5.png", "bg6.png",
            "bg7.png", "bg8.png", "bg9.png"
    };

    public ArenaSelectionPanel() {

        setLayout(new GridBagLayout());
        setBackground(new Color(5, 8, 25));

        // Keep the menu music playing
        MenuMusic.play();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
                "DUEL ARENA", SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.CYAN);

        JLabel subtitle = new JLabel(
                "CHOOSE YOUR BATTLE BACKGROUND",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.BOLD, 15));
        subtitle.setForeground(Color.LIGHT_GRAY);

        JLabel p1Label = new JLabel("PLAYER 1 - BLUE");
        p1Label.setForeground(Color.CYAN);
        p1Label.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel p2Label = new JLabel("PLAYER 2 - RED");
        p2Label.setForeground(Color.PINK);
        p2Label.setFont(new Font("Arial", Font.BOLD, 16));

        player1Arena = new JComboBox<>(arenas);
        player2Arena = new JComboBox<>(arenas);

        player1Arena.setSelectedIndex(0);
        player2Arena.setSelectedIndex(1);

        JButton startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(15, 100, 140));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridy = 1;
        add(subtitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(p1Label, gbc);

        gbc.gridx = 1;
        add(p2Label, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(player1Arena, gbc);

        gbc.gridx = 1;
        add(player2Arena, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(startButton, gbc);

        startButton.addActionListener(e -> {

            String p1 =
                    (String) player1Arena.getSelectedItem();

            String p2 =
                    (String) player2Arena.getSelectedItem();

            JFrame frame = (JFrame)
                    SwingUtilities.getWindowAncestor(this);

            if (frame == null) {
                return;
            }

            // Stop music when the battle starts
            MenuMusic.stop();

            GamePanel gamePanel = new GamePanel(p1, p2);

            frame.setContentPane(gamePanel);
            frame.revalidate();
            frame.repaint();

            gamePanel.requestFocusInWindow();
        });
    }
}
