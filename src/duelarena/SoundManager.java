
package duelarena;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    // Two bullet sound effects
    private static Clip[] sounds = new Clip[2];

    // Separate music tracks
    private static Clip backgroundMusic;
    private static Clip victoryMusic;

    // Sound settings
    private static boolean backgroundMusicEnabled = true;
    private static boolean bulletSoundsEnabled = true;

    // Track which screen's music should play
    private static boolean victoryScreenActive = false;

    // Load all sounds
    public static void loadSounds() {
        loadBulletSound(0, "resources/gunshoot.wav");
        loadBulletSound(1, "resources/gunshoot2.wav");

        loadMusic("resources/battle_music.wav", false);
        loadMusic("resources/victory_music.wav", true);
    }

    // Load a music file
    private static void loadMusic(String filePath, boolean victory) {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("Music file not found: " + filePath);
                return;
            }

            try (AudioInputStream stream =
                         AudioSystem.getAudioInputStream(file)) {

                Clip clip = AudioSystem.getClip();
                clip.open(stream);

                if (victory) {
                    victoryMusic = clip;
                } else {
                    backgroundMusic = clip;
                }
            }

        } catch (Exception e) {
            System.out.println("Could not load music: " + filePath);
            e.printStackTrace();
        }
    }

    // Load a bullet sound
    private static void loadBulletSound(int index, String filePath) {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("Bullet sound not found: " + filePath);
                return;
            }

            try (AudioInputStream stream =
                         AudioSystem.getAudioInputStream(file)) {

                sounds[index] = AudioSystem.getClip();
                sounds[index].open(stream);
            }

        } catch (Exception e) {
            System.out.println("Could not load bullet sound: " + filePath);
            e.printStackTrace();
        }
    }

    // Play battle music
    public static void playBackgroundMusic() {
        victoryScreenActive = false;
        stopClip(victoryMusic);

        if (!backgroundMusicEnabled || backgroundMusic == null) {
            return;
        }

        if (!backgroundMusic.isRunning()) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Play victory music instead of battle music
    public static void playVictoryMusic() {
        victoryScreenActive = true;
        stopClip(backgroundMusic);
        stopClip(victoryMusic);

        if (!backgroundMusicEnabled || victoryMusic == null) {
            return;
        }

        victoryMusic.setFramePosition(0);
        victoryMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Stop a music clip safely
    private static void stopClip(Clip clip) {
        if (clip != null && clip.isOpen()) {
            clip.stop();
            clip.setFramePosition(0);
        }
    }

    // Turn music ON or OFF
    public static void setBackgroundMusicEnabled(boolean enabled) {
        backgroundMusicEnabled = enabled;

        if (!enabled) {
            stopClip(backgroundMusic);
            stopClip(victoryMusic);
        } else {
            if (victoryScreenActive) {
                playVictoryMusic();
            } else {
                playBackgroundMusic();
            }
        }
    }

    // Check music setting
    public static boolean isBackgroundMusicEnabled() {
        return backgroundMusicEnabled;
    }

    // Play bullet sound independently
    public static void playSound(int index) {
        if (!bulletSoundsEnabled) {
            return;
        }

        if (index < 0 || index >= sounds.length) {
            return;
        }

        Clip sound = sounds[index];

        if (sound == null || !sound.isOpen()) {
            return;
        }

        sound.stop();
        sound.setFramePosition(0);
        sound.start();
    }

    // Turn bullet sounds ON or OFF independently
    public static void setBulletSoundsEnabled(boolean enabled) {
        bulletSoundsEnabled = enabled;
    }

    // Check bullet sound setting
    public static boolean areBulletSoundsEnabled() {
        return bulletSoundsEnabled;
    }

    // Stop both music tracks when leaving the game
    public static void stopBackgroundMusic() {
        stopClip(backgroundMusic);
        stopClip(victoryMusic);
        victoryScreenActive = false;
    }

    // Stop and release audio resources
    public static void stopAllSounds() {
        stopBackgroundMusic();

        for (Clip sound : sounds) {
            if (sound != null) {
                sound.stop();
                sound.close();
            }
        }

        if (backgroundMusic != null) {
            backgroundMusic.close();
            backgroundMusic = null;
        }

        if (victoryMusic != null) {
            victoryMusic.close();
            victoryMusic = null;
        }
    }
}
