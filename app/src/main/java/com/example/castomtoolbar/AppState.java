package com.example.castomtoolbar;

public class AppState {

    boolean soundOn = true;
    int numberOfDisks;
    int stepsTaken = 0;
    private int screenWidth;
    private int screenHeight;


    int container_max_w; //left screen edge to guideline_vertical_left
    int container_max_h; //guideline_horizontal_bottom to guideline_horizontal_top



    private AppState() {
    }

    private static AppState instance = null;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return(instance);
    }


    public int getContainer_max_w() {
        return container_max_w;
    }

    public int getContainer_max_h() {
        return container_max_h;
    }

    public void setContainer_max_w(int container_max_w) {
        this.container_max_w = container_max_w;
    }

    public void setContainer_max_h(int container_max_h) {
        this.container_max_h = container_max_h;
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
