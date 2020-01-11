package com.example.castomtoolbar;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class HelpActivity extends ToolbarActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        toolbar = (Toolbar) findViewById(R.id.bar_of_tools);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public int getMenuResourceID() {
        return R.menu.menu_help;
    }

    @Override
    public void restartGame() {

    }


}
