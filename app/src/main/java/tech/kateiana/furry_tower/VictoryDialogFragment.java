package tech.kateiana.furry_tower;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class VictoryDialogFragment extends DialogFragment implements View.OnClickListener {


    private TextView youDidItInTextview;
    private TextView yourBestResultTextview;
    private TextView bestPossibleResultTextview;

    private ImageView homeButton;
    private ImageView playAgainButton;
    private ImageView nextLevetButton;

    AppState appState;
    private PrefManager prefManager;

    GameAttributes attributes;

    public VictoryDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static VictoryDialogFragment newInstance(String title) {
        VictoryDialogFragment frag = new VictoryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog_fragment, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        appState = AppState.getInstance();
        attributes = new GameAttributes();

        youDidItInTextview = view.findViewById(R.id.you_did_it_in_textview);
        setYouDidItInTextview();

        yourBestResultTextview = view.findViewById(R.id.your_best_result_textview);
        setYourBestResultTextview();

        bestPossibleResultTextview = view.findViewById(R.id.best_possible_result_textview);
        setBestPossibleResultTextview();

        homeButton = view.findViewById(R.id.new_home_button_imageView);
        homeButton.setOnClickListener(this);

        playAgainButton = view.findViewById(R.id.new_play_again_button_imageView);
        playAgainButton.setOnClickListener(this);

        nextLevetButton = view.findViewById(R.id.new_next_lvl_button_imageView);
        if(appState.currentLevel == attributes.getMaxLevel())
        {
            nextLevetButton.setVisibility(View.GONE);
        }
        else
        {
            nextLevetButton.setOnClickListener(this);
        }

    }


    private void setYouDidItInTextview() {
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

    }

    private void setYourBestResultTextview() {

        prefManager = new PrefManager(getContext());
        int usersBestResult = prefManager.getBestResult(appState.currentLevel);
        yourBestResultTextview.setText(getResources().getString(R.string.best_result_in_victory_dialog)
                + " " + usersBestResult + " " +  getResources().getString(R.string.moves_in_victory_dialog));

    }

    private void setBestPossibleResultTextview() {
        int bestPossibleResult = attributes.bestPossibleResults[appState.getCurrentLevel()];
        bestPossibleResultTextview.setText(getResources().getString(R.string.best_possible_result_in_victory_dialog)
                + " " + bestPossibleResult + " " + getResources().getString(R.string.moves_in_victory_dialog));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.new_home_button_imageView:
                Intent home = new Intent(getContext(), MainActivity.class);
                startActivity(home);
                break;
            case R.id.new_play_again_button_imageView:
                restartLevel();
                break;
            case R.id.new_next_lvl_button_imageView:
                startNextLevel();
                break;
        }
    }

    private void startNextLevel() {

        if(appState.currentLevel < attributes.getMaxLevel())
        {
            appState.setCurrentLevel(appState.currentLevel + 1);
            ((GameActivity)getActivity()).restartGame();
            dismiss();
        }
        else
        {
            restartLevel();
        }
    }

    private void restartLevel() {
        ((GameActivity)getActivity()).restartGame();
        dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();


        int[] screenWandH = ((GameActivity)getActivity()).measureScreen();
        int screenW = screenWandH[0];
        int screenH = screenWandH[1];

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//        params.width = LayoutParams.MATCH_PARENT;
//        params.height = LayoutParams.MATCH_PARENT;
        params.width = screenW;
        params.height = screenH;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
