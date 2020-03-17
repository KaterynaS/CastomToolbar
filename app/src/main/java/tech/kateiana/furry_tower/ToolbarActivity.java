package tech.kateiana.furry_tower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

public abstract class ToolbarActivity extends AppCompatActivity {

    Menu mainMenu;
    int menuResourceID;
    AppState appState;
    MenuItem soundMenuItem;

    private boolean isHome = true;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuResourceID = getMenuResourceID();
        mainMenu = menu;

        getMenuInflater().inflate(menuResourceID, menu);
        appState = AppState.getInstance();

        updateSoundButton();

        soundMenuItem = mainMenu.findItem(R.id.sound_menu_item);

        if(mainMenu.findItem(R.id.sound_menu_item) != null)
        {
            if(appState.isSoundOn()) { soundMenuItem.setIcon(R.drawable.ico_music_90x90); }
            else { soundMenuItem.setIcon(R.drawable.ico_music_mute_90x90); }
        }

        return true;
    }

    public void updateSoundButton()
    {
        soundMenuItem = mainMenu.findItem(R.id.sound_menu_item);

        appState = AppState.getInstance();

        if(appState.isSoundOn())
        {
            soundMenuItem.setIcon(R.drawable.ico_music_90x90);
        }
        else
        {
            soundMenuItem.setIcon(R.drawable.ico_music_mute_90x90);
        }
    }

    public void openHelpPage()
    {
        //todo open help slider https://www.javatpoint.com/android-introduction-slider-example
        Intent tutorial = new Intent(getApplicationContext(), TutorialActivity.class);
        startActivity(tutorial);
    }

    public abstract int getMenuResourceID();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_menu_item:
                openHelpPage();
                break;
            case R.id.sound_menu_item:
                turnSound();
                break;
            case R.id.restart_menu_item:
                restartGame();
                break;
            case  R.id.about_menu_item:
                openAboutPage();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAboutPage() {
        Intent about = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(about);
    }

    public void updateStepsCounterTextview() {
        MenuItem counterMenuItem = mainMenu.findItem(R.id.counter_menu_item);

        appState = AppState.getInstance();

        String steps = getString(R.string.moves_toolbar) + " " + appState.getStepsTaken();
        counterMenuItem.setTitle(steps);
    }

    public abstract void restartGame();

    private void turnSound() {
        appState = AppState.getInstance();

        if(appState.isSoundOn())
        {
            soundMenuItem.setIcon(R.drawable.ico_music_mute_90x90);
            appState.setSoundOff();
            MusicService.onPause();
        }
        else
        {
            soundMenuItem.setIcon(R.drawable.ico_music_90x90);
            appState.setSoundOn();
            MusicService.onResume();
        }
    }



    @Override
    protected void onResume() {
        System.gc();
        super.onResume();

        appState = AppState.getInstance();
        if(appState.isSoundOn())
        {
            MusicService.onResume();
        }

        isHome = true;
    }

    @Override
    protected void onPause() {
        if (((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getCallState()==TelephonyManager.CALL_STATE_RINGING
                || !((PowerManager)getSystemService(POWER_SERVICE)).isScreenOn()) {
            MusicService.onPause();
        }
        super.onPause();
        System.gc();
    }

    @Override
    public boolean onKeyDown (final int keyCode, final KeyEvent ke) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                isHome = false;
            default:
                return super.onKeyDown(keyCode, ke);
        }
    }

    @Override
    public void startActivity(final Intent i) {
        isHome = false;
        super.startActivity(i);
    }

    @Override
    protected void onUserLeaveHint() {
        if (isHome) {
            MusicService.onPause();
        }
        super.onUserLeaveHint();
    }

}





