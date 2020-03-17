package tech.kateiana.furry_tower;

public class AppState {

    private boolean isSoundOn = true;
    int currentLevel;
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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
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

    public boolean isSoundOn() {
        return isSoundOn;
    }

    public void setSoundOn() {
        isSoundOn = true;
    }

    public void setSoundOff() {
        isSoundOn = false;
    }

}
