
package duelarena;

import javax.sound.sampled.*;
import java.io.File;

public class MenuMusic {

    private static Clip menuClip;
    private static Clip gameOverClip;

    private static boolean musicEnabled = true;

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void toggleMusic() {
        musicEnabled = !musicEnabled;

        if (!musicEnabled) {
            stopMenuMusic();
            stopGameOverMusic();
        }
    }

    public static void play() {
        if (!musicEnabled) {
            return;
        }

        try {
            if (menuClip == null) {
                File file = new File(
                        "resources/menu_music/main music.wav"
                );

                if (!file.exists()) {
                    System.out.println(
                            "Menu music file not found: "
                                    + file.getAbsolutePath()
                    );
                    return;
                }

                try (AudioInputStream audio =
                             AudioSystem.getAudioInputStream(file)) {
                    menuClip = AudioSystem.getClip();
                    menuClip.open(audio);
                }
            }

            if (!menuClip.isRunning()) {
                if (menuClip.getFramePosition()
                        >= menuClip.getFrameLength()) {
                    menuClip.setFramePosition(0);
                }

                menuClip.loop(Clip.LOOP_CONTINUOUSLY);
            }

        } catch (Exception e) {
            System.out.println("Menu music could not play.");
            e.printStackTrace();
        }
    }

    public static void stop() {
        stopMenuMusic();
    }

    private static void stopMenuMusic() {
        if (menuClip != null && menuClip.isRunning()) {
            menuClip.stop();
        }
    }

    public static void playGameOverMusic() {
        stopMenuMusic();

        if (!musicEnabled) {
            return;
        }

        try {
            if (gameOverClip == null) {
                File file = new File(
                        "resources/gameover_music/gameover.wav"
                );

                if (!file.exists()) {
                    System.out.println(
                            "Game Over music not found: "
                                    + file.getAbsolutePath()
                    );
                    return;
                }

                try (AudioInputStream audio =
                             AudioSystem.getAudioInputStream(file)) {
                    gameOverClip = AudioSystem.getClip();
                    gameOverClip.open(audio);
                }
            }

            stopGameOverMusic();
            gameOverClip.setFramePosition(0);
            gameOverClip.start();

        } catch (Exception e) {
            System.out.println("Game Over music could not play.");
            e.printStackTrace();
        }
    }

    private static void stopGameOverMusic() {
        if (gameOverClip != null && gameOverClip.isRunning()) {
            gameOverClip.stop();
        }
    }
}
