package com.kateandyana.hanoi_tower;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends ToolbarActivity implements View.OnDragListener {

    AppState appState;

    ConstraintLayout mainParentLayout;

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

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        appState = AppState.getInstance();

        mainParentLayout = findViewById(R.id.game_activity_layout);

        toolbarGame = findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbarGame);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);

        woohooSoundMediaPlayer = MediaPlayer.create(this, R.raw.cartoon_woohoo);
        uhahSoundMediaPlayer = MediaPlayer.create(this, R.raw.uh_oh);
        context = GameActivity.this;

        mActivity = GameActivity.this;

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
        for (int i = appState.getCurrentLevel()-1; i >= 0; i--) {
            //create an text view of a disk, starting with the biggest

            //todo set target_1_imageview size as a disk height

            final ImageView imageDisk = new ImageView(getApplicationContext());
            imageDisk.setImageDrawable(getResources().getDrawable(ga.disksImgResourcesList[i]));

            int maxDiscWidthInPx = (int) (appState.getScreenWidth()*0.3 - 16); //16 is a left margin
            int maxDiscHeightInPx = (int) (appState.getScreenHeight()*0.7 - 48)/8; //48 is a toolbar height


            LinearLayout.LayoutParams diskParams = new LinearLayout.LayoutParams(maxDiscWidthInPx, maxDiscHeightInPx);
            if(i == appState.getCurrentLevel()-1) { diskParams.setMargins(0,-5,0,-3); }
            else {diskParams.setMargins(0,-10,0,-3);}
            imageDisk.setLayoutParams(diskParams);

            imageDisk.setTag(""+i);

            imageDisk.setOnTouchListener(new MyTouchListener());

            //add view to the pole
            startPole.addView(imageDisk,0);

            targetSlot1 = findViewById(R.id.target_1_imageview);
            targetSlot2 = findViewById(R.id.target_2_imageview);

            int pyramidHeight = maxDiscHeightInPx * appState.getCurrentLevel();
            int pyramidHeightInPercentsOfScreenHeight = 100*pyramidHeight/appState.getScreenHeight();

            guidelineHorizontalBottom = findViewById(R.id.guideline_horizontal_bottom);
            ConstraintLayout.LayoutParams guidelineHorizontalBottomParams =
                    (ConstraintLayout.LayoutParams) guidelineHorizontalBottom.getLayoutParams();


            float guidelineHorizontalBottomHeightInPercents = guidelineHorizontalBottomParams.guidePercent;

            float newTopGuidlineHeight = (guidelineHorizontalBottomHeightInPercents*100 - pyramidHeightInPercentsOfScreenHeight)/100;

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

        int currentLvl = appState.currentLevel;
        int maxLevel;

        AlertDialog.Builder builder
                = new AlertDialog.Builder(GameActivity.this, R.style.CustomAlertDialogStyle);
        final View view = getLayoutInflater().inflate(R.layout.dialog_solid_background, null);

        final AlertDialog dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);


        TextView buttonText = view.findViewById(R.id.next_level_button_textview);
        ImageView circleArrow = view.findViewById(R.id.circle_arrow_imageview);

        GameAttributes attributes = new GameAttributes();
        maxLevel = attributes.getMaxLevel();

        if(currentLvl == maxLevel)
        {
            circleArrow.setVisibility(View.VISIBLE);
            buttonText.setText(R.string.play_again_in_victory_dialog);
        }
        else if (currentLvl < maxLevel)
        {
            currentLvl = currentLvl + 1;
        }


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
        // lp.dimAmount=0.5f;
        lp.width = (int) dialogWidth;
        lp.height = (int) dialogHeight;

        TextView bestMovesTextview = view.findViewById(R.id.best_result_textview);
        TextView bestPossibleResultTextView = view.findViewById(R.id.best_possible_result_textview);


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mainParentLayout.setForeground(new ColorDrawable(getColor(R.color.dim_background)));
        }

        mainParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mainParentLayout.setForeground(new ColorDrawable(Color.TRANSPARENT));
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        int bestResult = getBestResult();

        bestMovesTextview.setText(getResources().getString(R.string.best_result_in_victory_dialog)
                + " " + bestResult + " " +  getResources().getString(R.string.moves_in_victory_dialog));


        int bestPossibleResult = attributes.bestPossibleResults[appState.getCurrentLevel()];
        bestPossibleResultTextView.setText(getResources().getString(R.string.best_possible_result_in_victory_dialog)
                + " " + bestPossibleResult + " " + getResources().getString(R.string.moves_in_victory_dialog));




        TextView youDidItInTextview = view.findViewById(R.id.you_did_it_in_textview);


        int vdGreenColor = getResources().getColor(R.color.victory_dialog_green);
        int vdGrayColor = getResources().getColor(R.color.victory_dialog_gray);


        String part1 = getResources().getString(R.string.you_did_it_in_victory_dialog);
        Spannable part1span = new SpannableString(part1);
        part1span.setSpan(new ForegroundColorSpan(vdGrayColor), 0, part1span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        youDidItInTextview.setText(part1span);

        int stepsTaken = appState.stepsTaken;
        Spannable steps = new SpannableString(" " + stepsTaken + " ");
        steps.setSpan(new ForegroundColorSpan(vdGreenColor), 0, steps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        youDidItInTextview.append(steps);

        String b = getResources().getString(R.string.moves_in_victory_dialog);
        Spannable movesString = new SpannableString(b);
        movesString.setSpan(new ForegroundColorSpan(vdGrayColor), 0, movesString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        youDidItInTextview.append(movesString);

        //number color #306D18

        appState.setCurrentLevel(currentLvl);
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

        //get tag of a top view
        String tagOfTopView = "";
        tagOfTopView = tagOfTopView + acceptingContainer.getChildAt(0).getTag();
        int topViewValue = Integer.valueOf(tagOfTopView);

        //get tag of dragged disk
        View v = (View) event.getLocalState();
        String tagOfDraggedDisk = v.getTag().toString();
        int draggedViewValue = Integer.valueOf(tagOfDraggedDisk);

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

        if(targetPole.getChildCount() == appState.getCurrentLevel()
                || helpPole.getChildCount() == appState.getCurrentLevel())
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
//        //counter = 0;
//        Log.d("GameActivity", "restartGame");
//        appState = AppState.getInstance();
//        clearPoles();
//        placeTargets();
//        setOnListeners();
//        createTower();
//
//        updateStepsCounterTextview();
        restartActivity();
    }


    public void restartActivity() {

        Activity activity = GameActivity.this;

        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
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
                break;
            }
        }
        return false;
    }

    public void saveResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (appState.getCurrentLevel()) {
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
    }

    public int getBestResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switch (appState.getCurrentLevel()) {
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
