package com.kateandyana.hanoi_tower;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;



    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    private static final String BEST_RESULT_3_DISCS = "steps3";
    private static final String BEST_RESULT_4_DISCS = "steps4";
    private static final String BEST_RESULT_5_DISCS = "steps5";
    private static final String BEST_RESULT_6_DISCS = "steps6";
    private static final String BEST_RESULT_7_DISCS = "steps7";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setBestResult(int level, int steps)
    {
        if(getBestResult(level) == 0 || getBestResult(level) > steps)
        {
            switch (level)
            {
                case 3:
                    editor.putInt(BEST_RESULT_3_DISCS, steps);
                    break;
                case 4:
                    editor.putInt(BEST_RESULT_4_DISCS, steps);
                    break;
                case 5:
                    editor.putInt(BEST_RESULT_5_DISCS, steps);
                    break;
                case 6:
                    editor.putInt(BEST_RESULT_6_DISCS, steps);
                    break;
                case 7:
                    editor.putInt(BEST_RESULT_7_DISCS, steps);
                    break;
                default:
                    break;
            }
        }
        editor.commit();
    }

    public int getBestResult(int level)
    {
        int bestResultSteps = 0;

        switch (level)
        {
            case 3:
                bestResultSteps = pref.getInt(BEST_RESULT_3_DISCS, 0);
                break;
            case 4:
                bestResultSteps = pref.getInt(BEST_RESULT_4_DISCS, 0);
                break;
            case 5:
                bestResultSteps = pref.getInt(BEST_RESULT_5_DISCS, 0);
                break;
            case 6:
                bestResultSteps = pref.getInt(BEST_RESULT_6_DISCS, 0);
                break;
            case 7:
                bestResultSteps = pref.getInt(BEST_RESULT_7_DISCS, 0);
                break;
            default:
                break;
        }

        return bestResultSteps;
    }


    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
