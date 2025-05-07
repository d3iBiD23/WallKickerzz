package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Score {
    private BitmapFont font;
    private BitmapFont smallFont; // Fuente más pequeña para el high score
    private GlyphLayout layout;
    private int score;
    private float highestY;
    private int highScore;
    private GlyphLayout smallLayout;

    public Score() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("font/Schoolbell-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter param =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Fuente grande para el score actual
        param.size = 120;
        param.color = Color.WHITE;
        param.borderWidth = 8;
        param.borderColor = Color.BLACK;
        font = gen.generateFont(param);

        // Fuente más pequeña para el high score
        param.size = 60;
        smallFont = gen.generateFont(param);

        gen.dispose();

        layout = new GlyphLayout();
        smallLayout = new GlyphLayout();
        score = 0;
        highScore = PreferencesManager.getHighScore();
        highestY = 0f;
    }

    public void update(float playerY) {
        if (playerY > highestY) {
            highestY = playerY;
            score = (int) highestY;

            // Actualizar high score si es necesario
            if (score > highScore) {
                highScore = score;
                PreferencesManager.saveHighScore(highScore);
            }
        }
    }

    public void render(SpriteBatch batch, float viewportWidth, float viewportHeight) {
        // Renderizar score actual
        String scoreText = "Score: " + score;
        layout.setText(font, scoreText);
        float scoreX = (viewportWidth - layout.width) / 2f;
        float scoreY = viewportHeight - layout.height - 150f;
        font.draw(batch, layout, scoreX, scoreY);

        // Renderizar high score debajo
        String highScoreText = "High Score: " + highScore;
        smallLayout.setText(smallFont, highScoreText);
        float highScoreX = (viewportWidth - smallLayout.width) / 2f;
        float highScoreY = scoreY - smallLayout.height - 30f;
        smallFont.draw(batch, smallLayout, highScoreX, highScoreY);
    }

    public int getCurrentScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void dispose() {
        font.dispose();
        smallFont.dispose();
    }
}
