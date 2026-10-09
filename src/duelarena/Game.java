package duelarena;

import javax.swing.JFrame;

public class Game {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Duel Arena");

        frame.setSize(800, 600);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        // Allow resizing and maximizing
        frame.setResizable(true);

        GamePanel gamePanel = new GamePanel();

        frame.add(gamePanel);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        gamePanel.requestFocusInWindow();
    }
}