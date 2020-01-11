package com.example.castomtoolbar;

public class AppState {

    boolean soundOn = true;

    private AppState() {

    }

    private static AppState instance = null;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return(instance);
    }

    public void turnSound()
    {
        if(soundOn)
        {
            soundOn = false;
        }
        else
        {
            soundOn = true;
        }
    }

    public boolean isSoundOn() {
        return soundOn;
    }

}
