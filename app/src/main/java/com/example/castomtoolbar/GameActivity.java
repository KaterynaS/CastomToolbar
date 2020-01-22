package com.example.castomtoolbar;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

public class GameActivity extends ToolbarActivity implements View.OnDragListener {

    AppState appState;

    Context context;
    MediaPlayer mp;

    LinearLayout startPole;
    LinearLayout helpPole;
    LinearLayout targetPole;

    Toolbar toolbarGame;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String BEST_RESULT_3_DISCS = "steps3";
    public static final String BEST_RESULT_4_DISCS = "steps4";
    public static final String BEST_RESULT_5_DISCS = "steps5";
    public static final String BEST_RESULT_6_DISCS = "steps6";
    public static final String BEST_RESULT_7_DISCS = "steps7";

    private int currentGameBestResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        appState = AppState.getInstance();

        toolbarGame = findViewById(R.id.bar_of_tools);
        setSupportActionBar(toolbarGame);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mp = MediaPlayer.create(this, R.raw.sound_click);
        context = GameActivity.this;

        findViews();

        setOnListeners();
        createTower();
    }

    private void createTower() {
        GameAttributes ga = new GameAttributes();
        //fill starting pole with corresponding amount of disks
        for (int i = appState.getNumberOfDisks()-1; i >= 0; i--) {
            //create an text view of a disk, starting with the biggest

            ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));
            imageDisk.setTag(""+i);

            imageDisk.setOnTouchListener(new MyTouchListener());

            //add view to the pole
            startPole.addView(imageDisk,0);
            appState.resetSteps();
            //todo updateStepCount
        }
    }

    private void setOnListeners() {
        startPole.setOnDragListener(this);
        helpPole.setOnDragListener(this);
        targetPole.setOnDragListener(this);
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
                    && isOnTop(view)) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isOnTop(View view) {
        boolean onTop = false;

        LinearLayout parent = (LinearLayout) view.getParent();
        int a = parent.indexOfChild(view);

        if(a == 0)
        {
            onTop = true;
        }
        else
        {
            wrongMoveAction();
        }
        return onTop;
    }

    private void wrongMoveAction() {
        Toast.makeText(context, R.string.cant_move_item, Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        //v.vibrate(100);
        //todo ability to turn off vibration or turn it off with sound
    }

    private void clickSound() {
        if(appState.isSoundOn())
        {
            //soundButton.refreshDrawableState();
            mp.start();
        }
    }

    private void victoryAction() {

        saveResult();

        String bestResult = "\nBest result: " + getBestResult() + " steps";

        String yourResult = "\nYour result: " + appState.getStepsTaken() + " steps";

        String message = getString(R.string.congrat_message) + yourResult + bestResult;

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("victory")
                .setNegativeButton("quit", null)
                .setPositiveButton("more", null)
                .show();


        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                dialog.dismiss();
            }
        });

        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    finishAffinity();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
            }
        });

    }

    private boolean draggedIsSmallerThanTop(View view, DragEvent event) {
        boolean isSmallerOnTop;
        LinearLayout acceptingContainer = (LinearLayout) view;
        //count views in acceptingContainer
        Log.d("draggedIsSmallerThanTop", "num of views: " + acceptingContainer.getChildCount());

        //get tag of a top view
        String tagOfTopView = "";
        tagOfTopView = tagOfTopView + acceptingContainer.getChildAt(0).getTag();
        int topViewValue = Integer.valueOf(tagOfTopView);
        Log.d("draggedIsSmallerThanTop", "tag of a top view: " + tagOfTopView);

        //get tag of dragged disk
        View v = (View) event.getLocalState();
        String tagOfDraggedDisk = v.getTag().toString();
        int draggedViewValue = Integer.valueOf(tagOfDraggedDisk);
        Log.d("draggedIsSmallerThanTop", "tag of a dargged view: " + tagOfDraggedDisk);

        if(draggedViewValue < topViewValue)
        {
            isSmallerOnTop = true;
        }
        else
        {
            isSmallerOnTop = false;
            wrongMoveAction();
        }

        return isSmallerOnTop;
    }

    private boolean dropEventNotHandled(DragEvent dragEvent)
    {
        return !dragEvent.getResult();
    }

    private boolean isOrderCorrect() {
        boolean isCorrect = false;

        if(targetPole.getChildCount() == appState.getNumberOfDisks()
                || helpPole.getChildCount() == appState.getNumberOfDisks())
        {
            isCorrect = true;
        }

        return isCorrect;
    }

    private void findViews() {
        startPole = findViewById(R.id.start_pole_ll);
        helpPole = findViewById(R.id.help_pole_ll);
        targetPole = findViewById(R.id.target_pole_ll);

        startPole.setTag("start");
        helpPole.setTag("help");
        targetPole.setTag("target");
    }

    @Override
    public int getMenuResourceID() {
        return R.menu.menu_game;
    }

    @Override
    public void restartGame() {
        //counter = 0;
        Log.d("GameActivity", "restartGame");
        clearPoles();
        createTower();
        updateStepsCounterTextview();
        //todo
    }

    private void clearPoles() {
        startPole.removeAllViews();
        helpPole.removeAllViews();
        targetPole.removeAllViews();
    }

    @Override
    public boolean onDrag(View view, DragEvent event) {
        // Defines a variable to store the action type for the incoming event
        int action = event.getAction();
        // Handles each of the expected events
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
            {
                // Determines if this View can accept the dragged data
                return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
            }
            case DragEvent.ACTION_DRAG_ENTERED:
            {
                return true;
            }
            case DragEvent.ACTION_DRAG_LOCATION:
            {
                // Ignore the event
                return true;
            }
            case DragEvent.ACTION_DRAG_EXITED:
            {
                return true;
            }

            case DragEvent.ACTION_DROP:
            {
                // Gets the item containing the dragged data
                ClipData.Item item = event.getClipData().getItemAt(0);

                // Gets the text data from the item.
                String dragData = item.getText().toString();

                View draggedView = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) draggedView.getParent();
                LinearLayout acceptor = (LinearLayout) view;

                //remove the dragged view
                owner.removeView(draggedView);

                if(acceptor.getChildCount() == 0)
                {
                    acceptor.addView(draggedView, 0);

                    if(!acceptor.getTag().equals(owner.getTag()))
                    {
                        appState.addOneStep();
                        updateStepsCounterTextview();
                    }

                }
                else  if(acceptor.getChildCount() > 0 && draggedIsSmallerThanTop(view, event))
                {
                    acceptor.addView(draggedView, 0);
                    if(!acceptor.getTag().equals(owner.getTag()))
                    {
                        appState.addOneStep();
                        updateStepsCounterTextview();
                    }
                }
                else
                {
                    Log.d("ACTION_DROP", "last text: " + "empty");
                    Log.d("ACTION_DROP", "dragged text: " + dragData);
                    //Add the dragged view
                    owner.addView(draggedView, 0);
                }
                draggedView.setVisibility(View.VISIBLE);

                if(isOrderCorrect())
                {
                    victoryAction();
                    //customDialog();
                }
                return true;
            }

            case DragEvent.ACTION_DRAG_ENDED:
            {
                view.invalidate();
                clickSound();

                if (dropEventNotHandled(event))
                {
                    View vi = (View) event.getLocalState();
                    vi.setVisibility(View.VISIBLE);
                }
                // returns true; the value is ignored.
                return true;
            }

            // An unknown action type was received.
            default:
            {
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
            }
        }
        return false;
    }

    public void saveResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //todo save steps depending on discs number
//
//        editor.putInt()
//        editor.putString(TEXT, textView.getText().toString());
//        editor.putBoolean(SWITCH1, switch1.isChecked());


        switch (appState.getNumberOfDisks()) {
            case 3:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_3_DISCS, 0);
                if (currentGameBestResult == 0 || currentGameBestResult > appState.getStepsTaken())
                {
                    editor.putInt(BEST_RESULT_3_DISCS, appState.getStepsTaken());
                }
                break;
            case 4:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_4_DISCS, 0);
                if (currentGameBestResult == 0 || currentGameBestResult > appState.getStepsTaken())
                {
                    editor.putInt(BEST_RESULT_4_DISCS, appState.getStepsTaken());
                }
                break;
            case 5:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_5_DISCS, 0);
                if (currentGameBestResult == 0 || currentGameBestResult > appState.getStepsTaken())
                {
                    editor.putInt(BEST_RESULT_5_DISCS, appState.getStepsTaken());
                }
                break;
            case 6:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_6_DISCS, 0);
                if (currentGameBestResult == 0 || currentGameBestResult > appState.getStepsTaken())
                {
                    editor.putInt(BEST_RESULT_6_DISCS, appState.getStepsTaken());
                }
                break;
            case 7:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_7_DISCS, 0);
                if (currentGameBestResult == 0 || currentGameBestResult > appState.getStepsTaken())
                {
                    editor.putInt(BEST_RESULT_7_DISCS, appState.getStepsTaken());
                }
                break;
            default:
                break;
        }

        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public int getBestResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switch (appState.getNumberOfDisks()) {
            case 3:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_3_DISCS, 0);
                break;
            case 4:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_4_DISCS, 0);
                break;
            case 5:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_5_DISCS, 0);
                break;
            case 6:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_6_DISCS, 0);
                break;
            case 7:
                currentGameBestResult = sharedPreferences.getInt(BEST_RESULT_7_DISCS, 0);
                break;
            default:
                break;
        }

        return currentGameBestResult;
    }

}
