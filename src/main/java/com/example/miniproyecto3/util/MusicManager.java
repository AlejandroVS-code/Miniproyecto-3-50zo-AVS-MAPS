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
    private static final float DEFAULT_VOLUME = 0.06f;

    private static final float SFX_VOLUME = 0.3f;


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

    /**
     * Reproduce un efecto de sonido corto (click, hover, etc.) sin afectar
     * la música de fondo. Usa un Clip independiente y no hace loop.
     * @param resourcePath ruta del recurso, ej: "/com/example/miniproyecto3/Audio/click.wav"
     */
    public static void playSoundEffect(String resourcePath) {
        try {
            InputStream is = MusicManager.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("[MusicManager] Efecto no encontrado: " + resourcePath);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(is));
            Clip effectClip = AudioSystem.getClip();
            effectClip.open(audioStream);

            if (effectClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl =
                        (FloatControl) effectClip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log10(Math.max(SFX_VOLUME, 0.0001f)) * 20);
                gainControl.setValue(Math.max(gainControl.getMinimum(),
                        Math.min(gainControl.getMaximum(), dB)));
            }

            // liberar recursos del clip cuando termine de sonar
            effectClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    effectClip.close();
                }
            });

            effectClip.start();
        } catch (Exception e) {
            e.printStackTrace();
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