package tech.kateiana.furry_tower;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {

    MediaPlayer mediaPlayer;

    AppState appState;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        appState = AppState.getInstance();

        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);


    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        //todo
        if(appState.isSoundOn())
        {
            mediaPlayer.start();
            appState.setSoundOn();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Music turned off",    Toast.LENGTH_SHORT).show();
        }


        return startId;
    }

    public void onStart(Intent intent, int startId) {
        appState.setSoundOn();
        mediaPlayer.start();
    }

    @Override
    public ComponentName startService(Intent service) {
        appState.setSoundOn();
        mediaPlayer.start();
        return super.startService(service);
    }

    @Override
    public void onDestroy() {
        appState.setSoundOff();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }

    @Override
    public boolean stopService(Intent name) {
        appState.setSoundOff();
        mediaPlayer.stop();
        mediaPlayer.release();
        return super.stopService(name);
    }


}
