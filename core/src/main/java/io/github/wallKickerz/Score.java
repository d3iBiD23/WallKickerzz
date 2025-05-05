package io.github.wallKickerz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Score {
    private BitmapFont font;
    private GlyphLayout layout;
    private int score;
    private float highestY;

    public Score() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
            Gdx.files.internal("font/Schoolbell-Regular.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter param =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 120;  // Tamaño de la fuente más grande
        param.color = Color.ROYAL;  // Color de la fuente (negro para contraste)
        font = gen.generateFont(param);
        gen.dispose();

        layout = new GlyphLayout();
        score = 0;
        highestY = 0f;
    }

    public void update(float playerY) {
        if (playerY > highestY) {
            highestY = playerY;
            score = (int) highestY;
        }
    }

    public void render(SpriteBatch batch, float viewportWidth, float viewportHeight) {
        String text = "Score: " + score;
        layout.setText(font, text);
        float x = (viewportWidth - layout.width) / 2f;
        float y = viewportHeight - layout.height - 150f;  // Posición abajo de la pantalla
        font.draw(batch, layout, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}
