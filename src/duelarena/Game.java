
package duelarena;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Game {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                // Use the computer's normal window appearance.
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );

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

                System.out.println(
                        "Duel Arena window opened successfully."
                );

            } catch (Exception e) {
                System.err.println(
                        "Error while opening Duel Arena:"
                );
                e.printStackTrace();
            }
        });
    }
}
