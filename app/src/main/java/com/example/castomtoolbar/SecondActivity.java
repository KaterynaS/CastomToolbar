package com.example.castomtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class SecondActivity extends AppCompatActivity {

    Toolbar toolbarSecond;

    Button addOne;
    int counter;

    Menu toolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        toolbarSecond = (Toolbar) findViewById(R.id.bar_of_tools);
        setSupportActionBar(toolbarSecond);
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
        MenuItem stepCounter = toolbarMenu.findItem(R.id.counter);
        String s = "Steps: " + counter;
        stepCounter.setTitle(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }
}
