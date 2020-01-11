package com.example.castomtoolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class GameActivity extends ToolbarActivity {

    Toolbar toolbarGame;
    Button addOne;
    int counter;

    Menu toolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        toolbarGame = (Toolbar) findViewById(R.id.bar_of_tools);
        setSupportActionBar(toolbarGame);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addOne = findViewById(R.id.add_one_button);
        addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                Log.d("addOne", "counter = " + counter);
                updateStepCounter();
            }
        });
    }

    private void updateStepCounter() {
        toolbarMenu = toolbarGame.getMenu();
        MenuItem stepCounter = toolbarMenu.findItem(R.id.counter_menu_item);
        String s = "Steps: " + counter;
        stepCounter.setTitle(s);
    }

    @Override
    public int getMenuResourceID() {
        return R.menu.menu_game;
    }

    @Override
    public void restartGame() {
        counter = 0;
        updateStepCounter();
    }
}
