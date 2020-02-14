package com.example.castomtoolbar;

public class AppState {

    boolean soundOn = true;
    int numberOfDisks;
    int stepsTaken = 0;
    private int screenWidth;
    private int screenHeight;


    private AppState() {
    }

    private static AppState instance = null;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return(instance);
    }

    public int getStepsTaken() {
        return stepsTaken;
    }

    public void resetSteps() {
        this.stepsTaken = 0;
    }

    public void addOneStep()
    {
        stepsTaken++;
    }

    public int getNumberOfDisks() {
        return numberOfDisks;
    }

    public void setNumberOfDisks(int numberOfDisks) {
        this.numberOfDisks = numberOfDisks;
    }

    public void turnSound()
    {
        soundOn = !soundOn;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }


}
