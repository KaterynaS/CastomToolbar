package tech.kateiana.furry_tower;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

public class CarefulMediaPlayer {

    final SharedPreferences sp;
    final MediaPlayer mp;
    public boolean isPlaying = false;
    private static CarefulMediaPlayer instance;



    public CarefulMediaPlayer(final MediaPlayer mp, final MusicService ms) {
        sp = PreferenceManager.getDefaultSharedPreferences(ms.getApplicationContext());
        this.mp = mp;
        instance = this;
    }


    public static CarefulMediaPlayer getInstance() {
        return instance;
    }

    public void start() {
        if (sp.getBoolean("com.embed.candy.music", true) && !isPlaying) {
            mp.start();
            isPlaying = true;
        }
    }

    public void pause() {
        if (isPlaying) {
            mp.pause();
            isPlaying = false;
        }
    }

    public void stop() {
        isPlaying = false;
        try {
            mp.stop();
            mp.release();
        } catch (final Exception e) {}
    }
}
