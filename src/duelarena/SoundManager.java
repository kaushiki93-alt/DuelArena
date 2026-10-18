
package duelarena;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    // Store two different bullet sounds
    private static Clip[] sounds = new Clip[2];

    // Load bullet sound files
    public static void loadSounds() {

        String[] files = {
                "resources/gunshoot2.wav",
                "resources/gunshoot.wav"
        };

        for (int i = 0; i < files.length; i++) {

            try {
                File soundFile = new File(files[i]);

                AudioInputStream audioStream =
                        AudioSystem.getAudioInputStream(soundFile);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                sounds[i] = clip;

                audioStream.close();

                System.out.println(
                        "Sound loaded successfully: " + files[i]
                );

            } catch (Exception e) {

                System.out.println(
                        "Could not load sound: " + files[i]
                );

                e.printStackTrace();
            }
        }
    }

    // Play the selected bullet sound
    public static void playBulletSound(int soundChoice) {

        int index = soundChoice - 1;

        if (index >= 0 && index < sounds.length
                && sounds[index] != null) {

            Clip clip = sounds[index];

            if (clip.isRunning()) {
                clip.stop();
            }

            clip.setFramePosition(0);
            clip.start();
        }
    }
}
