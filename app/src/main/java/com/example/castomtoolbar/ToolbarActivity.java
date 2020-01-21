package com.example.castomtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public abstract class ToolbarActivity extends AppCompatActivity {

    Toolbar toolbar;
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
            if(appState.isSoundOn()) { soundMenuItem.setIcon(R.drawable.ic_sound_on); }
            else { soundMenuItem.setIcon(R.drawable.ic_sound_off); }
        }
        return true;
    }

    public void openHelpPage()
    {
        //todo open help slider https://www.javatpoint.com/android-introduction-slider-example
        Intent welcome = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(welcome);
    }

    public abstract int getMenuResourceID();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_menu_item:
                openHelpPage(); //Is this a good way to do it? Yes, here it's okey!
                break;
            case R.id.sound_menu_item:
                turnSound();
                break;
            case R.id.restart_menu_item:
                restartGame();
                break;
            case  R.id.close_menu_item:
                //closeHelp();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateStepsCounterTextview() {
        //todo move it to toolbar class
        MenuItem counterMenuItem = mainMenu.findItem(R.id.counter_menu_item);

        AppState appState = AppState.getInstance();

        String steps = "Steps: " + appState.getStepsTaken();
//        Toast.makeText(getApplicationContext(), steps,
//                Toast.LENGTH_SHORT).show();
        counterMenuItem.setTitle(steps);
    }

    public void closeHelp()
    {
        //todo
        // onBackPressed();
    }

    public abstract void restartGame();

    private void turnSound() {
        AppState appState = AppState.getInstance();
        appState.turnSound();

        MenuItem soundMenuItem = mainMenu.findItem(R.id.sound_menu_item);
        String soundStatus = "";
        if(appState.isSoundOn())
        {
            soundStatus = "on";
            soundMenuItem.setIcon(R.drawable.ic_sound_on);
        }
        else
        {
            soundStatus = "off";
            soundMenuItem.setIcon(R.drawable.ic_sound_off);
        }

        Toast.makeText(getApplicationContext(), "sound " + soundStatus,
                Toast.LENGTH_SHORT).show();
    }
}
