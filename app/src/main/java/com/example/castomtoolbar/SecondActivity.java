package com.example.castomtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class SecondActivity extends AppCompatActivity {

    Button mainActivity;
    Toolbar toolbarSecond;

    Button addOne;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mainActivity = findViewById(R.id.main_activity_button);
        mainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(main);
            }
        });

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
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }
}
