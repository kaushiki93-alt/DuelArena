
package duelarena;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public final class VoiceManager {

    private static final ExecutorService voiceQueue =
            Executors.newSingleThreadExecutor(task -> {
                Thread thread = new Thread(task, "DuelArena-Voice");
                thread.setDaemon(true);
                return thread;
            });

    private static volatile boolean voiceEnabled = true;
    private static volatile boolean shuttingDown = false;

    // Changes whenever the current speech sequence is cancelled.
    private static final AtomicLong speechVersion = new AtomicLong(0);

    private static volatile Process currentProcess;

    private VoiceManager() {
    }

    // Queue a normal announcement without blocking gameplay.
    public static void speak(String message) {
        if (!voiceEnabled || shuttingDown ||
                message == null || message.isBlank()) {
            return;
        }

        long version = speechVersion.get();
        submitSpeech(message.trim(), version);
    }

    // Speak and return a future that completes when the speech finishes.
    public static CompletableFuture<Void> speakAndWait(String message) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!voiceEnabled || shuttingDown ||
                message == null || message.isBlank()) {
            future.complete(null);
            return future;
        }

        long version = speechVersion.get();

        try {
            voiceQueue.submit(() -> {
                try {
                    if (version == speechVersion.get()
                            && voiceEnabled && !shuttingDown) {
                        speakNow(message.trim(), version);
                    }
                    future.complete(null);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
        } catch (java.util.concurrent.RejectedExecutionException e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    private static void submitSpeech(String message, long version) {
        try {
            voiceQueue.submit(() -> {
                if (version != speechVersion.get()
                        || !voiceEnabled || shuttingDown) {
                    return;
                }

                speakNow(message, version);
            });
        } catch (java.util.concurrent.RejectedExecutionException e) {
            System.err.println("[VoiceManager] Voice queue is closed.");
        }
    }

    private static void speakNow(String text, long version) {
        Process process = null;

        try {
            String encoded = Base64.getEncoder().encodeToString(
                    text.getBytes(StandardCharsets.UTF_8)
            );

            String script =
                    "Add-Type -AssemblyName System.Speech; " +
                            "$text = [System.Text.Encoding]::UTF8.GetString(" +
                            "[System.Convert]::FromBase64String('" + encoded + "')); " +
                            "$s = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                            "try { " +
                            "$s.Volume = 100; " +
                            "$s.Rate = 0; " +
                            "$s.Speak($text); " +
                            "} finally { $s.Dispose(); }";

            ProcessBuilder builder = new ProcessBuilder(
                    "powershell.exe",
                    "-NoProfile",
                    "-Command",
                    script
            );

            builder.redirectErrorStream(true);
            process = builder.start();
            currentProcess = process;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream(),
                            StandardCharsets.UTF_8))) {

                while (reader.readLine() != null) {
                    // Consume output so the process cannot block.
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0 && version == speechVersion.get()) {
                System.err.println(
                        "[VoiceManager] Speech failed. Exit code: " + exitCode
                );
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            if (process != null) {
                process.destroyForcibly();
            }

        } catch (Exception e) {
            System.err.println("[VoiceManager] Could not play speech.");

            if (process != null) {
                process.destroyForcibly();
            }

        } finally {
            if (currentProcess == process) {
                currentProcess = null;
            }
        }
    }

    // Cancel queued and currently playing speech.
    public static void cancelSpeech() {
        speechVersion.incrementAndGet();

        Process process = currentProcess;
        if (process != null && process.isAlive()) {
            process.destroyForcibly();
        }
    }

    public static void setVoiceEnabled(boolean enabled) {
        voiceEnabled = enabled;

        if (!enabled) {
            cancelSpeech();
        }
    }

    public static boolean isVoiceEnabled() {
        return voiceEnabled;
    }

    // Call only when the entire application is closing.
    public static void shutdown() {
        shuttingDown = true;
        cancelSpeech();
        voiceQueue.shutdownNow();
    }
}
