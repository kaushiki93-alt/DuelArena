
package duelarena;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Duel Arena");

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);

            MainMenuPanel mainMenu =
                    new MainMenuPanel(frame);

            frame.setContentPane(mainMenu);
            frame.setVisible(true);
        });
    }
}
