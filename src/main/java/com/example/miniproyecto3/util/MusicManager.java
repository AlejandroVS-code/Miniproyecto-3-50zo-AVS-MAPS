package com.example.miniproyecto3.util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Manages background music and short sound effects using
 * {@code javax.sound.sampled}.
 *
 * Supports three looping background tracks (menu, game, end screen) and
 * any number of short, non-looping sound effects (hover, click, turn), each
 * with its own independently configurable volume. Sound effects are played
 * on their own {@link Clip} instances so they never interrupt or get
 * interrupted by the currently playing background track.
 *
 * @author Maria Alejandra Pizarro Sarria
 * @author Alejandro Valencia Sandoval
 * @version 1.0
 */

public class MusicManager {

    private static Clip clip;

    /** Volume applied to background music tracks (0.0f = silent, 1.0f = max). */
    private static final float DEFAULT_VOLUME = 0.06f;

    /** Volume applied to short sound effects (0.0f = silent, 1.0f = max). */
    private static final float SFX_VOLUME = 0.3f;


    /**
     * Plays the given audio file on an infinite loop as the background
     * track, stopping any track that was previously playing.
     *
     * @param resourcePath classpath resource path, e.g. {@code "/com/example/miniproyecto3/Audio/menu.wav"}.
     */
    public static void playMusic(String resourcePath) {
        stopMusic();
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

    /** Stops and releases the currently playing background track, if any. */
    public static void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    /**
     * Plays a short sound effect (click, hover, etc.) without affecting the
     * background music. Uses an independent, non-looping {@link Clip} that
     * closes itself automatically once playback finishes.
     *
     * @param resourcePath classpath resource path, e.g. {@code "/com/example/miniproyecto3/Audio/click.wav"}.
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

    /**
     * Adjusts the volume of the currently playing background track.
     *
     * @param volume the desired volume, from {@code 0.0f} (silent) to {@code 1.0f} (max).
     */
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