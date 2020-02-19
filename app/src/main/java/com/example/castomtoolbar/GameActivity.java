package com.example.castomtoolbar;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends ToolbarActivity implements View.OnDragListener {

    AppState appState;

    Context context;
    MediaPlayer woohooSoundMediaPlayer;
    MediaPlayer uhahSoundMediaPlayer;

    LinearLayout startPole;
    LinearLayout helpPole;
    LinearLayout targetPole;

    ImageView targetSlot1;
    ImageView targetSlot2;

    Toolbar toolbarGame;

    Guideline guidelineHorizontalTop;
    Guideline guidelineHorizontalBottom;
    Guideline guidelineVerticalLeft;
    Guideline guidelineVerticalRight;

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

        toolbarGame = findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbarGame);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);

        woohooSoundMediaPlayer = MediaPlayer.create(this, R.raw.cartoon_woohoo);
        uhahSoundMediaPlayer = MediaPlayer.create(this, R.raw.uh_oh);
        context = GameActivity.this;

        findViews();

        placeTargets();

        setOnListeners();
        createTower();
    }

    private void placeTargets() {

        targetSlot1 = findViewById(R.id.target_1_imageview);
        targetSlot2 = findViewById(R.id.target_2_imageview);

        //choose randomly from target list

        GameAttributes ga = new GameAttributes();

        ArrayList<Integer> targetIndexes = new ArrayList<>();
        for (int i = 0; i < ga.targetImgResourceList.length; i++) {
            targetIndexes.add(i);
        }

        Random rand = new Random();
        int targetOneIndex = rand.nextInt(targetIndexes.size());
        targetIndexes.remove(targetOneIndex);
        int targetTwoIndex = targetIndexes.get(rand.nextInt(targetIndexes.size()));

        Log.d("placeTargets", "\ntargetOneIndex = " + targetOneIndex
            + "\ntargetTwoIndex = " + targetTwoIndex);

        int maxDiscHeightInPx = (int) (appState.getScreenHeight()*0.7 - 48)/8;

        ConstraintLayout.LayoutParams targetOneLayoutParams = (ConstraintLayout.LayoutParams) targetSlot1.getLayoutParams();
        targetOneLayoutParams.height = maxDiscHeightInPx;
        targetSlot1.setLayoutParams(targetOneLayoutParams);
        targetSlot1.setImageDrawable(getResources().getDrawable(ga.targetImgResourceList[targetOneIndex]));


        ConstraintLayout.LayoutParams targetTwoLayoutParams = (ConstraintLayout.LayoutParams) targetSlot2.getLayoutParams();
        targetTwoLayoutParams.height = maxDiscHeightInPx;
        targetSlot2.setLayoutParams(targetTwoLayoutParams);
        targetSlot2.setImageDrawable(getResources().getDrawable(ga.targetImgResourceList[targetTwoIndex]));
    }

    private void createTower() {
        GameAttributes ga = new GameAttributes();
        //fill starting pole with corresponding amount of disks
        for (int i = appState.getNumberOfDisks()-1; i >= 0; i--) {
            //create an text view of a disk, starting with the biggest

            //todo set target_1_imageview size as a disk height

            final ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));

            int maxDiscWidthInPx = (int) (appState.getScreenWidth()*0.3 - 16); //16 is a left margin
            int maxDiscHeightInPx = (int) (appState.getScreenHeight()*0.7 - 48)/8; //48 is a toolbar height
            Log.d("updateDiskPyramid", "\nmaxDiscWidthInPx = " + maxDiscWidthInPx
                    + "\nmaxDiscHeightInPx = " + maxDiscHeightInPx);


            LinearLayout.LayoutParams diskParams = new LinearLayout.LayoutParams(maxDiscWidthInPx, maxDiscHeightInPx);
            if(i == appState.getNumberOfDisks()-1) { diskParams.setMargins(0,-5,0,0); }
            else {diskParams.setMargins(0,-10,0,0);}
            imageDisk.setLayoutParams(diskParams);

            imageDisk.setTag(""+i);

            imageDisk.setOnTouchListener(new MyTouchListener());

            //add view to the pole
            startPole.addView(imageDisk,0);

//            targetSlot1 = findViewById(R.id.target_1_imageview);
//            targetSlot2 = findViewById(R.id.target_2_imageview);
//
/////////////////
//            ConstraintLayout layout = (ConstraintLayout) targetSlot1.getParent(); //??
//            ConstraintLayout.LayoutParams targetParams =
//                    (ConstraintLayout.LayoutParams) targetSlot1.getLayoutParams();
//            //targetParams.leftToLeft(guidelineVerticalRight);
//            targetParams = new ConstraintLayout.LayoutParams(3*maxDiscHeightInPx/2, 3*maxDiscHeightInPx/2);
//            targetSlot1.setLayoutParams(targetParams);


            //"measure" the pyramid and adjust target fruit height
            int pyramidHeight = maxDiscHeightInPx * appState.getNumberOfDisks();
            int pyramidHeightInPercentsOfScreenHeight = 100*pyramidHeight/appState.getScreenHeight();

            Log.d("measurements", "\nscreenHeight in px = " + appState.getScreenHeight());
            Log.d("measurements", "\npyramidHeight in px = " + pyramidHeight
                    + "\npyramidHeightInPercentsOfScreenHeight = " + pyramidHeightInPercentsOfScreenHeight);

            guidelineHorizontalBottom = findViewById(R.id.guideline_horizontal_bottom);
            ConstraintLayout.LayoutParams guidelineHorizontalBottomParams =
                    (ConstraintLayout.LayoutParams) guidelineHorizontalBottom.getLayoutParams();


            float guidelineHorizontalBottomHeightInPercents = guidelineHorizontalBottomParams.guidePercent;
            Log.d("measurements", "\nguidelineHorizontalBottomHeightInPercents = " + guidelineHorizontalBottomHeightInPercents);

            float newTopGuidlineHeight = (guidelineHorizontalBottomHeightInPercents*100 - pyramidHeightInPercentsOfScreenHeight)/100;

            Log.d("measurements", "\nnewTopGuidlineHeight % = " + newTopGuidlineHeight);

            guidelineHorizontalTop = findViewById(R.id.guideline_horizontal_top);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guidelineHorizontalTop.getLayoutParams();
            params.guidePercent = newTopGuidlineHeight; // 45% // range: 0 <-> 1
            guidelineHorizontalTop.setLayoutParams(params);

            appState.resetSteps();
        }
    }

    private void victoryAction()
    {
        saveResult();

        AlertDialog.Builder builder
                = new AlertDialog.Builder(GameActivity.this, R.style.CustomAlertDialogStyle);
        View view = getLayoutInflater().inflate(R.layout.dialog_constrain, null);



        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeightPixels = displayMetrics.heightPixels;
        int screenWidthPixels = displayMetrics.widthPixels;

        double dialogWidth;
        double dialogHeight;


        if(screenHeightPixels < 1500)
        {
             dialogWidth = screenWidthPixels*7/10;
             dialogHeight = screenHeightPixels*5/10;
        }
        else
        {
             dialogWidth = screenWidthPixels*6/10;
             dialogHeight = screenHeightPixels*4/10;
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.dimAmount=0.5f;
        lp.width = (int) dialogWidth;
        lp.height = (int) dialogHeight;

        TextView movesTextview = view.findViewById(R.id.number_of_moves_textview);
        TextView bestMovesTextview = view.findViewById(R.id.best_result_textview);


        TextView homeButton = view.findViewById(R.id.home_button_textview);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout playAgainButton = view.findViewById(R.id.play_again_button_linearlayout);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(R.color.dim_background);



        int a = appState.stepsTaken;
        int b = getBestResult();

        Log.d("buttonDialog", "steps taken = " + a + "; best = " + b);

        movesTextview.setText(appState.stepsTaken + "");
        bestMovesTextview.setText(getResources().getString(R.string.best_result_in_victory_dialog)
                + " " + b + " " +  getResources().getString(R.string.moves_in_victory_dialog));
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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
        if(appState.isSoundOn())
        {
            uhahSoundMediaPlayer.start();
        }

        //Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 400 milliseconds
        //v.vibrate(100);
        //todo ability to turn off vibration or turn it off with sound
    }

    private void discDroppedSound() {
        if(appState.isSoundOn())
        {
            //soundButton.refreshDrawableState();
            woohooSoundMediaPlayer.start();
        }
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

        guidelineVerticalRight = findViewById(R.id.guideline_vertical_right);
        guidelineVerticalLeft = findViewById(R.id.guideline_vertical_left);
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

        View draggedView = (View) event.getLocalState();
        ViewGroup owner = (ViewGroup) draggedView.getParent();
        LinearLayout acceptor = (LinearLayout) view;

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

                //remove the dragged view
                owner.removeView(draggedView);

                if(acceptor.getChildCount() == 0)
                {
                    acceptor.addView(draggedView, 0);

                    if(!acceptor.getTag().equals(owner.getTag()))
                    {
                        appState.addOneStep();
                        discDroppedSound();
                        updateStepsCounterTextview();
                    }

                }
                else  if(acceptor.getChildCount() > 0 && draggedIsSmallerThanTop(view, event))
                {
                    acceptor.addView(draggedView, 0);
                    if(!acceptor.getTag().equals(owner.getTag()))
                    {
                        appState.addOneStep();
                        discDroppedSound();
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

                if (dropEventNotHandled(event))
                {
                    View vi = (View) event.getLocalState();
                    vi.setVisibility(View.VISIBLE);
                }

                view.invalidate();

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
        //Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
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
