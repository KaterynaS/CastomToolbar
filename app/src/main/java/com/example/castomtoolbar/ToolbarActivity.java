package com.example.castomtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
    MenuItem finger;
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



        //popup window
        // inflate the layout of the popup window
//        LayoutInflater inflater = (LayoutInflater)
//                getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.popup_window, null);
//
//        // create the popup window
//        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window tolken
//        View view = findViewById(R.id.bar_of_tools);
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupWindow.dismiss();
//                return true;
//            }
//        });
    }

    public void onButtonShowPopupWindowClick(View view) {

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
                closeHelp();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
