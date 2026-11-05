
package duelarena;

import javax.swing.JOptionPane;

public class VoiceTest {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("   DUEL ARENA VOICE TEST");
        System.out.println("================================");

        // Test 1: Basic announcement.
        VoiceManager.speak(
                "Welcome to Duel Arena!"
        );

        // Test 2: Countdown announcement.
        VoiceManager.speak(
                "Three, two, one! Start the battle!"
        );

        // Test 3: Read a name entered by the tester.
        String player1Name = JOptionPane.showInputDialog(
                null,
                "Enter Player 1 name:"
        );

        if (player1Name == null || player1Name.isBlank()) {
            player1Name = "Player One";
        }

        // Test 4: Read another independently entered name.
        String player2Name = JOptionPane.showInputDialog(
                null,
                "Enter Player 2 name:"
        );

        if (player2Name == null || player2Name.isBlank()) {
            player2Name = "Player Two";
        }

        player1Name = player1Name.trim();
        player2Name = player2Name.trim();

        // Test 5: Both names must be spoken dynamically.
        VoiceManager.speak(
                player1Name + ", protect yourself!"
        );

        VoiceManager.speak(
                player2Name + ", protect yourself!"
        );

        // Test 6: Adaptive balancing announcement.
        VoiceManager.speak(
                "Adaptive balancing activated!"
        );

        // Test 7: Winner announcement using the entered name.
        VoiceManager.speak(
                "Congratulations, " + player2Name + "! You won!"
        );

        System.out.println("Voice requests submitted.");
        System.out.println("Listen for all announcements in order.");

        // Keep this test application alive until the queue finishes.
        VoiceManager.shutdown();
    }
}
