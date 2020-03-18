package tech.kateiana.furry_tower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends ToolbarActivity {

    Toolbar toolbarAbout;
    TextView kateianaSiteLink;
    TextView musicSiteLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbarAbout = findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbarAbout);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);

        kateianaSiteLink = findViewById(R.id.kateiana_site_link_text);
        kateianaSiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kateiana.tech"));
                startActivity(browserIntent);
                CarefulMediaPlayer.getInstance().pause();
            }
        });

        musicSiteLink = findViewById(R.id.music_site_link);
        musicSiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fesliyanstudios.com"));
                startActivity(browserIntent);
                CarefulMediaPlayer.getInstance().pause();
            }
        });

    }

    @Override
    public int getMenuResourceID() {
        return R.menu.menu_about;
    }

    @Override
    public void restartGame() {

    }


    @Override
    protected void onResume() {
        super.onResume();

        AppState appState = AppState.getInstance();

        if(appState.isSoundOn())
        {

            CarefulMediaPlayer.getInstance().start();
        }

    }
}
