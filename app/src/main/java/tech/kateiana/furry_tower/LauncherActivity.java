package tech.kateiana.furry_tower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        else
        {

            prefManager.setFirstTimeLaunch(false);
            Intent tutorial = new Intent(LauncherActivity.this, TutorialActivity.class);
            tutorial.putExtra("fromLauncher", 1);
            startActivity(tutorial);
        }



    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        finish();

    }
}
