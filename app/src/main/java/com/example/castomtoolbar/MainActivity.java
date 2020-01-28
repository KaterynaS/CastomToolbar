package com.example.castomtoolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends ToolbarActivity {

    Button gameButton;
    Toolbar toolbarMain;
    AppState appState;


    //building pyramid
    Button addDiskButton;
    Button subtractDiskButton;
    int currentNumberOfDisks = 3; //from 3 to 7
    LinearLayout poleLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appState = AppState.getInstance();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        appState.setScreenHeight(screenHeight);
        appState.setScreenWidth(screenWidth);


        //toolbar
        toolbarMain = findViewById(R.id.toolbar_widget).findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbarMain);

        //building pyramid
        findViews();
        updateDiskPyramid();
        handleClicks();

        //"play" button
        gameButton = findViewById(R.id.game_button);
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState appState = AppState.getInstance();
                appState.setNumberOfDisks(currentNumberOfDisks);
                Intent second = new Intent(MainActivity.this, GameActivity.class);
                startActivity(second);
            }
        });
    }

    @Override
    public int getMenuResourceID() {
        return R.menu.menu_main;
    }

    @Override
    public void restartGame() { }


    //building pyramid methods
    private void handleClicks() {
        addDiskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDisk();
            }
        });

        subtractDiskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractDisk();
            }
        });
    }


    private void createTower()
    {
        GameAttributes ga = new GameAttributes();
        //fill starting pole with corresponding amount of disks
        for (int i = currentNumberOfDisks-1; i >= 0; i--) {
            //create an text view of a disk, starting with the biggest

            ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));
            imageDisk.setTag(""+i);

            //imageDisk.setOnLongClickListener(this);

            //add view to the pole
            poleLinearLayout.addView(imageDisk,0);
        }
    }


    private void addDisk()
    {
        if(currentNumberOfDisks < 7)
        {
            currentNumberOfDisks = currentNumberOfDisks + 1;
            updateDiskPyramid();
        }
    }

    private void subtractDisk()
    {
        if(currentNumberOfDisks > 3)
        {
            currentNumberOfDisks = currentNumberOfDisks - 1;
            updateDiskPyramid();
        }
    }

    private void updateDiskPyramid()
    {
        poleLinearLayout.removeAllViews();
        //update pyramid appearance
        GameAttributes ga = new GameAttributes();
        //fill starting pole with corresponding amount of disks
        for (int i = currentNumberOfDisks-1; i >= 0; i--)
        {
            //create an text view of a disk, starting with the biggest

            ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));
            imageDisk.setTag(""+i);

            //imageDisk.setOnLongClickListener(this);

            //add view to the pole
            poleLinearLayout.addView(imageDisk,0);
        }
    }

    private void findViews() {
        addDiskButton = findViewById(R.id.add_disk_button);
        subtractDiskButton = findViewById(R.id.subtruct_disk_button);
        poleLinearLayout = findViewById(R.id.pole_linear_layout);
    }





}
