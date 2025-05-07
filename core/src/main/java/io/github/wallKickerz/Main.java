package io.github.wallKickerz;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private SpriteBatch batch;
    private int currentScore;
    private int highScore;
    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setCurrentScore(int score) {
        this.currentScore = score;
        if (score > highScore) {
            highScore = score;
            PreferencesManager.saveHighScore(highScore);
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
