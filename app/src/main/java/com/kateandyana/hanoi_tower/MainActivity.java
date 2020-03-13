package com.kateandyana.hanoi_tower;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.kateandyana.hanoi_tower.R;

//TODO: rename castom to custom


public class MainActivity extends ToolbarActivity {

    ImageButton playButton;
    Toolbar toolbarMain;
    AppState appState;

    ImageButton addDiskButton;
    ImageButton subtractDiskButton;
    int currentNumberOfDisks = 3; //from 3 to 7
    LinearLayout poleLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appState = AppState.getInstance();

        // Checking for first time launch - before calling setContentView()
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;

        appState.setScreenHeight(screenHeight);
        appState.setScreenWidth(screenWidth);


        //toolbar
        toolbarMain = findViewById(R.id.toolbar_widget).findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //building pyramid
        findViews();
        updateDiskPyramid();
        handleClicks();

        //"play" button
        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppState appState = AppState.getInstance();
                appState.setCurrentLevel(currentNumberOfDisks);
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


    private void addDisk()
    {
        if(currentNumberOfDisks < 7) //TODO: introduce const
        {
            currentNumberOfDisks = currentNumberOfDisks + 1;
            updateDiskPyramid();
        }
    }

    private void subtractDisk()
    {
        if(currentNumberOfDisks > 3) //TODO: introduce const, why 3, not 1?
        {
            currentNumberOfDisks = currentNumberOfDisks - 1;
            updateDiskPyramid();
        }
    }

    private void updateDiskPyramid()
    {
        // TODO: introduce const
        int maxDiscWidthInPx = (int) (appState.getScreenWidth()*0.55 - 16);
        int maxDiscHeightInPx = (int) (appState.getScreenHeight()*0.75 - 48)/7;

        poleLinearLayout.removeAllViews();
        //update pyramid appearance
        GameAttributes ga = new GameAttributes();
        //fill starting pole with corresponding amount of disks
        for (int i = currentNumberOfDisks-1; i >= 0; i--)
        {

            ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));
            imageDisk.setTag(""+i); //TODO: String.valueOf(i)

            //TODO: consts
            LinearLayout.LayoutParams diskParams = new LinearLayout.LayoutParams(maxDiscWidthInPx, maxDiscHeightInPx);
            diskParams.setMargins(0,-5,0,-3);
            imageDisk.setLayoutParams(diskParams);

            //add view to the pole
            poleLinearLayout.addView(imageDisk,0);
        }
    }

    private void findViews() {
        addDiskButton = findViewById(R.id.plus_button);
        subtractDiskButton = findViewById(R.id.minus_button);
        poleLinearLayout = findViewById(R.id.pole_linear_layout);
    }

}
