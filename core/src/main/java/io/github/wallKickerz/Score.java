package io.github.wallKickerz;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score {
    private BitmapFont font;
    private GlyphLayout layout;
    private int score;
    private float highestY;

    public Score() {
        font = new BitmapFont();               // fuente por defecto
        font.getData().setScale(2f);           // escala a tu gusto
        layout = new GlyphLayout();
        score = 0;
        highestY = 0f;
    }

    public void update(float playerY) {
        if (playerY > highestY) {
            highestY = playerY;
            score = (int) highestY;             // convertir la altura en puntos
        }
    }

    public void render(SpriteBatch batch, float x, float y) {
        String text = "Score: " + score;
        layout.setText(font, text);
        font.draw(batch, layout, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}
