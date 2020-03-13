package com.kateandyana.hanoi_tower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

public abstract class ToolbarActivity extends AppCompatActivity {

    Menu mainMenu;
    int menuResourceID;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuResourceID = getMenuResourceID();
        mainMenu = menu;

        getMenuInflater().inflate(menuResourceID, menu);

        AppState appState = AppState.getInstance();

        if(mainMenu.findItem(R.id.sound_menu_item) != null)
        {
            MenuItem soundMenuItem = mainMenu.findItem(R.id.sound_menu_item);
            if(appState.isSoundOn()) { soundMenuItem.setIcon(R.drawable.ico_music_90x90); }
            else { soundMenuItem.setIcon(R.drawable.ico_music_mute_90x90); }
        }

        return true;
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateStepsCounterTextview() {
        MenuItem counterMenuItem = mainMenu.findItem(R.id.counter_menu_item);

        AppState appState = AppState.getInstance();

        String steps = getString(R.string.moves_toolbar) + " " + appState.getStepsTaken();
        counterMenuItem.setTitle(steps);
    }

    public abstract void restartGame();

    private void turnSound() {
        AppState appState = AppState.getInstance();
        appState.turnSound();

        MenuItem soundMenuItem = mainMenu.findItem(R.id.sound_menu_item);
        if(appState.isSoundOn())
        {
            soundMenuItem.setIcon(R.drawable.ico_music_90x90);
        }
        else
        {
            soundMenuItem.setIcon(R.drawable.ico_music_mute_90x90);
        }
    }




}





