package com.example.miniproyecto3.util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Gestor de música de fondo usando javax.sound.sampled.
 * Soporta tres pistas: menu, game y end.
 */
public class MusicManager {

    private static Clip clip;

    private static final float DEFAULT_VOLUME = 0.05f;

    /**
     * Reproduce el archivo de audio indicado en bucle infinito.
     * @param resourcePath ruta del recurso, ej: "/com/example/miniproyecto3/Audio/menu.wav"
     */
    public static void playMusic(String resourcePath) {
        stopMusic(); // detener cualquier pista previa
        try {
            InputStream is = MusicManager.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("[MusicManager] Archivo no encontrado: " + resourcePath);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(is));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            setVolume(DEFAULT_VOLUME);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Detiene y libera la pista actual. */
    public static void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    /** Ajusta el volumen (0.0f = silencio, 1.0f = máximo). */
    public static void setVolume(float volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(volume, 0.0001f)) * 20);
            gainControl.setValue(Math.max(gainControl.getMinimum(),
                    Math.min(gainControl.getMaximum(), dB)));
        }
    }
}