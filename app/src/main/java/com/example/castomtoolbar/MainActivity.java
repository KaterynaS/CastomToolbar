package com.example.castomtoolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends ToolbarActivity {

    Button gameButton;
    Toolbar toolbarMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameButton = findViewById(R.id.game_button);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent second = new Intent(MainActivity.this, GameActivity.class);
                startActivity(second);
            }
        });

        toolbarMain = (Toolbar) findViewById(R.id.bar_of_tools);
        setSupportActionBar(toolbarMain);
    }

    @Override
    public int getMenuResourceID() {
        return R.menu.menu_main;
    }

    @Override
    public void restartGame() {

    }
}
