package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {
    private static final String PREF_NAME = "game_prefs";
    private static final String HIGH_SCORE_KEY = "high_score";

    public static void saveHighScore(int score) {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putInteger(HIGH_SCORE_KEY, score);
        prefs.flush();
    }

    public static int getHighScore() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        return prefs.getInteger(HIGH_SCORE_KEY, 0);
    }
}
