package sample;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;


public class Audio {

    private Clip clip;

    public Audio(InputStream inputStream) {

        try {
            InputStream bufferedIn = new BufferedInputStream(inputStream);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void play() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    void stop() {
        if (clip.isRunning())
            clip.stop();
    }

    void close() {
        stop();
        clip.close();
    }


}