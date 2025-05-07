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
    private static final float HIGH_SCORE_PADDING = 120f;

    public Score() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("font/Schoolbell-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter param =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Fuente grande para el high score
        param.size = 100;
        param.color = Color.WHITE;
        param.borderWidth = 8; // Borde grande para el high score
        param.borderColor = Color.BLACK;
        font = gen.generateFont(param);

        // Fuente más pequeña para el score (borde más pequeño)
        param.size = 90;
        param.borderWidth = 8; // Borde más pequeño para el high score
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
        // 1. Renderizar el high score arriba, con fuente grande
        String highScoreText = "Highest Score : " + highScore;
        layout.setText(font, highScoreText);
        float highScoreX = (viewportWidth - layout.width) / 2f;
        float highScoreY = viewportHeight - layout.height - 270f; // margen desde arriba
        font.draw(batch, layout, highScoreX, highScoreY);

        // 2. Renderizar el score actual más abajo, con fuente más pequeña
        String scoreText = "Score: " + score;
        smallLayout.setText(smallFont, scoreText);
        float scoreX = (viewportWidth - smallLayout.width) / 2f;
        float scoreY = highScoreY - smallLayout.height - HIGH_SCORE_PADDING;
        smallFont.draw(batch, smallLayout, scoreX, scoreY);
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
