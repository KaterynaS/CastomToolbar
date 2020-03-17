package tech.kateiana.furry_tower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class AboutActivity extends ToolbarActivity {

    Toolbar toolbarAbout;

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

    }

    @Override
    public int getMenuResourceID() {
        return R.menu.menu_about;
    }

    @Override
    public void restartGame() {

    }
}
